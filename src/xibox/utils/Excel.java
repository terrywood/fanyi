package xibox.utils;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellReference;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.tmatesoft.sqljet.core.SqlJetException;
import org.tmatesoft.sqljet.core.SqlJetTransactionMode;
import org.tmatesoft.sqljet.core.table.ISqlJetCursor;
import org.tmatesoft.sqljet.core.table.ISqlJetTable;
import org.tmatesoft.sqljet.core.table.SqlJetDb;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 14-2-25.
 */
public class Excel {
    private String[] municipality = new String[]{"北京","上海","天津","重庆"};
    public ArrayList<String> read(FileInputStream file){
        ArrayList<String> list = new ArrayList<String>();
        try {
            Workbook wb = WorkbookFactory.create(file);
            Sheet sheet1 = wb.getSheetAt(0);
            /*for (Row row : sheet1) {
                for (Cell cell : row) {
                    CellReference cellRef = new CellReference(row.getRowNum(), cell.getColumnIndex());
                    System.out.print(cellRef.formatAsString());
                    System.out.print(" - ");
                }
            }*/
            for(int i=2;i<sheet1.getLastRowNum();i++){
                Row row = sheet1.getRow(i);
                Cell cell = row.getCell(3);
                Object obj = this.getCellValue(cell);
                if(obj!=null){
                    //String address = obj.toString();
                  /*  String address  = (CommonUtils.parseAddress(obj));
                    list.add(address);*/
                   // System.out.println("-------------------");



                   /* if(StringUtils.startsWithAny(address,municipality)){
                        address = StringUtils.replaceOnce(address, " ", "\n");
                        if(StringUtils.substring(address,2,3).equals("市")){
                            address = StringUtils.replaceOnce(address,"市","");
                        }
                    }else{
                        address = StringUtils.replace(address, " ", "\n", 2);
                        address = StringUtils.replaceOnce(address, "\n", ",");
                    }
                    */

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        }
        return  list;
    }

    private Object getCellValue(Cell cell){
        if(cell ==null) return null;
        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_STRING:
                return (cell.getRichStringCellValue().getString());
            case Cell.CELL_TYPE_NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return (cell.getDateCellValue());
                } else {
                    return (cell.getNumericCellValue());
                }
            case Cell.CELL_TYPE_BOOLEAN:
                return (cell.getBooleanCellValue());
            case Cell.CELL_TYPE_FORMULA:
                return (cell.getCellFormula());
            default:
                return null;
        }
    }

    public static void main(String args[]) throws IOException, SqlJetException, JSONException {
        Translate translate = new Translate();
        Excel excel = new Excel();
        //ArrayList<String> list =excel.read(new FileInputStream(new File("D:\\Documents\\rikyWorg.xlsx")));
        ArrayList<String> list =excel.read(new FileInputStream(new File("C:\\Documents and Settings\\TNT\\My Documents\\0303.xlsx")));
       int i =0;
       for(String str :list){
            i++;
           if(i>5)break;
            System.out.println("----------------------------");
            System.out.println(str);
            JSONObject english = translate.translate(str);
            CommonUtils.parseAddressEn(english);
        /*    if(english!=null){
                JSONArray array = english.getJSONArray("trans_result");
                System.out.println(array);
                *//*if(array.length()==2){
                    String region1 = array.getJSONObject(0).getString("dst");
                    String region2 = array.getJSONObject(1).getString("dst");
                    System.out.print(region1+" & " + region2);
                }if(array.length()==1){
                    String region1 = array.getJSONObject(0).getString("dst");
                }*//*
            }*/

        }

        /*
        File dbFile = new File("terry.sqlite");
        SqlJetDb db = null;
        try {
            db = SqlJetDb.open(dbFile, true);
            // db.getOptions().setAutovacuum(true);
          *//*  db.beginTransaction(SqlJetTransactionMode.WRITE);
            try {
                db.getOptions().setUserVersion(1);
            } finally {
                db.commit();
            }*//*
        } catch (SqlJetException e) {
            e.printStackTrace();
        }


        Map<Long ,String> sheng = new HashMap<Long, String>();

        db.beginTransaction(SqlJetTransactionMode.READ_ONLY);
        ISqlJetTable table = db.getTable("region");
        //ISqlJetCursor cur = table.lookup("name_idx", "北京");
        ISqlJetCursor cur = table.lookup("region_parent_id_idx",1);
        // ISqlJetCursor cur = table.scope(  null, new Object[]{100}, new Object[]{200});
        while (!cur.eof()) {
            sheng.put(cur.getInteger("id"),cur.getString("name"));
*//*
            System.out.println("region_id:" + cur.getInteger("region_id") +
                    ", NAME:" + cur.getString("region_name") + " Level:"+cur.getInteger("level"));
*//*
            cur.next();
        }
        cur.close();*/



    }
}
