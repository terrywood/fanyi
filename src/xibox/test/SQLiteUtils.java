package xibox.test;

//import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FileUtils;
import org.tmatesoft.sqljet.core.SqlJetTransactionMode;
import org.tmatesoft.sqljet.core.table.ISqlJetTable;
import org.tmatesoft.sqljet.core.table.SqlJetDb;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;


public class SQLiteUtils {
    private List<String> tableList = null;

    private Map<String, String> tableSchemaMap = null;

    private Map<String, List<String>> tableDataMap = null;
    private Map<String, List<String>> tableIndexMap = null;
    private  String sqliteSourceFilePath ="D:\\develop\\workspace-idea\\xiaomi\\doc\\city.sql";

    private void insertDb(SqlJetDb sqlJetDb, List<?> list, String table) throws Exception {
        try {
            sqlJetDb.beginTransaction(SqlJetTransactionMode.WRITE);
            ISqlJetTable iSqlJetTable = sqlJetDb.getTable(table);
            for (int i = 0; i < list.size(); i++)
                iSqlJetTable.insertByFieldNames(this.retrieveObjectMap(list.get(i)));

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqlJetDb.commit();
        }
    }

    private Map<String, Object> retrieveObjectMap(Object object) throws Exception {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Map<String, Object> objectMap = new Hashtable<String, Object>();
        Method method[] = object.getClass().getDeclaredMethods();
        for (int i = 0; i < method.length; i++) {
            if (method[i].getName().startsWith("get")) {
                String tempKey = method[i].getName();
                tempKey = tempKey.substring(tempKey.lastIndexOf(".") + 1);
                tempKey = tempKey.substring(3);
                String key = "";
                for (int j = 0; j < tempKey.length(); j++)
                    if (j == 0)
                        key += tempKey.substring(0, 1).toLowerCase();
                    else if (Character.isUpperCase(tempKey.charAt(j)))
                        key += ("_" + tempKey.substring(j, j + 1).toLowerCase());
                    else if (Character.isDigit(tempKey.charAt(j)))
                        key += ("_" + tempKey.substring(j, j + 1).toLowerCase());
                    else
                        key += tempKey.substring(j, j + 1);

                if (object.getClass().getName().endsWith("MagazineImage") && key.equals("magazine_id"))
                    key = "focus_id";

                Object value = method[i].invoke(object);
                if (value instanceof Date)
                    value = simpleDateFormat.format((Date) value);
                if (value != null)
                    objectMap.put(key, value);
            }
        }
        return objectMap;
    }

    private File createNewRawDbFile(List<String> tableList) throws Exception {
        File tempDbFile = File.createTempFile("temp", System.currentTimeMillis() + ".sql");

        SqlJetDb sqlJetDb = SqlJetDb.open(tempDbFile, true);
        sqlJetDb.getOptions().setAutovacuum(true);
        sqlJetDb.beginTransaction(SqlJetTransactionMode.WRITE);
        try {
            sqlJetDb.getOptions().setUserVersion(1);
            for (int i = 0; i < tableList.size(); i++)
                this.createTableNIndex(sqlJetDb, tableList.get(i));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqlJetDb.commit();
        }

        return tempDbFile;
    }

    private void createTableNIndex(SqlJetDb sqlJetDb, String table) throws Exception {
       System.out.println("Create Table... " + table);
        sqlJetDb.createTable(this.tableSchemaMap.get(table));
        for (int j = 0; j < this.tableIndexMap.get(table).size(); j++) {
            System.out.println("Create Index... " + table);
            sqlJetDb.createIndex(this.tableIndexMap.get(table).get(j));
        }
        for (int j = 0; j < this.tableDataMap.get(table).size(); j++) {
            System.out.println("Insert data... " + table);
            sqlJetDb.createTrigger(this.tableDataMap.get(table).get(j)) ;
        }
    }

    private void parseSourceFile() throws Exception {
        this.tableList = new ArrayList<String>();
        this.tableSchemaMap = new HashMap<String, String>();
        this.tableIndexMap = new HashMap<String, List<String>>();
        this.tableDataMap = new HashMap<String, List<String>>();

        BufferedReader bufferedReader = new BufferedReader(new FileReader(this.sqliteSourceFilePath));
        String line = null;
        while ((line = bufferedReader.readLine()) != null) {
            line = line.trim();
            if (line.equals(""))
                continue;

            if (line.startsWith("#")) {
                String table = line.substring(1).trim();
                String line2 = null;
                String schema = "";
                while ((line2 = bufferedReader.readLine()) != null) {
                    line2 = line2.trim();
                    if (line2.equals(""))
                        continue;

                    schema += line2;
                    if (line2.endsWith(";"))
                        break;
                }

                line2 = null;
                List<String> indexList = new ArrayList<String>();
                List<String> dataList = new ArrayList<String>();
                while ((line2 = bufferedReader.readLine()) != null) {
                    line2 = line2.trim();
                    if (line2.toLowerCase().startsWith("create index"))
                        indexList.add(line2);
                    else if (line2.toLowerCase().startsWith("INSERT INTO"))
                        dataList.add(line2);
                    else
                        break;
                }

                this.tableList.add(table);
                this.tableSchemaMap.put(table, schema);
                this.tableDataMap.put(table, indexList);
                this.tableIndexMap.put(table, dataList);
            }
        }

        bufferedReader.close();
    }

    public  static  void main(String[] args) throws Exception {
        DecimalFormat malFormat=new DecimalFormat("#");
        double t = 15900260700d;
        System.out.println(t);
        System.out.println(malFormat.format(t));

   /*     SQLiteUtils utils = new SQLiteUtils();
        utils.parseSourceFile();
        System.out.println(utils.tableList);
        System.out.println(utils.tableSchemaMap);
        File tempDbFile =  utils.createNewRawDbFile(utils.tableList);
*/

        /*
        SqlJetDb sqlJetDb = SqlJetDb.open(tempDbFile, true);
        City city = new City();
        city.setDisplayOrder(1);
        city.setLevel(1);
        city.setRegionId(1);
        city.setRegionName("GZ");
        List<City> list = new ArrayList<City>();
        list.add(city);
        utils.insertDb(sqlJetDb,list,"city");*/
      /*  FileUtils.copyFile(tempDbFile, new File("terry.sqlite"));

        System.out.println(tempDbFile);*/
    }
}
