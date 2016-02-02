package xibox.test;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONWriter;
import org.tmatesoft.sqljet.core.SqlJetException;
import org.tmatesoft.sqljet.core.SqlJetTransactionMode;
import org.tmatesoft.sqljet.core.table.ISqlJetCursor;
import org.tmatesoft.sqljet.core.table.ISqlJetTable;
import org.tmatesoft.sqljet.core.table.SqlJetDb;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.*;
public class DateBaseUtils {
    public  static  void main(String[] args) throws Exception {
        File dbFile = new File("terry.sqlite");
        SqlJetDb db = null;
        try {
            db = SqlJetDb.open(dbFile, true);
           // db.getOptions().setAutovacuum(true);
          /*  db.beginTransaction(SqlJetTransactionMode.WRITE);
            try {
                db.getOptions().setUserVersion(1);
            } finally {
                db.commit();
            }*/
        } catch (SqlJetException e) {
            e.printStackTrace();
        }


        db.beginTransaction(SqlJetTransactionMode.WRITE);
        ISqlJetTable table = db.getTable("region");
         File json = new File("doc/tdlist.txt");
        JSONObject jsonObject = new JSONObject(FileUtils.readFileToString(json));


        Iterator iter = jsonObject.sortedKeys();
        while (iter.hasNext()){
            String id = (String)iter.next();
            JSONArray array = jsonObject.getJSONArray(id);
            table.insert(id, array.getString(1),array.getString(0),array.getString(2));
        }
        db.commit();
        // System.out.println(jsonObject);

        /*db.beginTransaction(SqlJetTransactionMode.READ_ONLY);
        ISqlJetTable table = db.getTable("city");
        ISqlJetCursor cur = table.lookup("name_idx", "北京");
        //ISqlJetCursor cur = table.order("level_idx");
       *//* for(String idx : table.getIndexesNames()){
            System.out.println(idx) ;
        }*//*

        // ISqlJetCursor cur = table.scope(  null, new Object[]{100}, new Object[]{200});
        while (!cur.eof()) {
            System.out.println("region_id:" + cur.getInteger("region_id") +
                    ", NAME:" + cur.getString("region_name") + " Level:"+cur.getInteger("level"));
            cur.next();
        }*/
    }
}
