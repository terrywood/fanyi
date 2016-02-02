package xibox.utils;

/**
 * Created by Administrator on 14-2-17.
 */

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.json.JSONObject;
import xibox.pojo.AddressBean;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;

class Note implements ActionListener,Runnable{
    private int i=0;
    JFrame frame = new JFrame("Translate");
    JTextArea textArea = new JTextArea(150,50);//定义文本区
    JPanel butp = new JPanel();   //创建一个面板，用于加载按钮组件
    JPanel jptp=new JPanel();
    JButton open = new JButton("选择文件");
    JButton save = new JButton("保存文件");
    JLabel label = new JLabel("现在没有打开的文件");
    JLabel remark = new JLabel();
    DecimalFormat malFormat=new DecimalFormat("#");

    JProgressBar jpBar= new JProgressBar(0,100);
    File file = null;
    //public Note(String test)  { }
    public Note()  {
       try {
            //UIManager .setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
            UIManager .setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (ClassNotFoundException e1) {
            e1.printStackTrace();
        } catch (InstantiationException e1) {
            e1.printStackTrace();
        } catch (IllegalAccessException e1) {
            e1.printStackTrace();
        } catch (UnsupportedLookAndFeelException e1) {
            e1.printStackTrace();
        }
        butp.add(this.open);//向面板中加入打开按钮
       // butp.add(this.save);//向面板中加入保存按钮
        jptp.add(this.jpBar);
        //jptp.setPreferredSize(new Dimension(30, 15));
        //this.frame.setLayout(new BorderLayout(4,3));//设置窗体的布局，并指定组件边框之间的间距
        this.frame.setLayout(new GridLayout(4,1,0,0));//设置窗体的布局，并指定组件边框之间的间距
        this.frame.add(this.label);
        this.frame.add(jptp);
        this.jpBar.setValue(0);
        this.jpBar.setStringPainted(true);
        Dimension d = new Dimension(600,20);
        this.jpBar.setPreferredSize(d);
        this.textArea.setLineWrap(true);
        this.frame.add(this.butp);

        //this.frame.add(new JScrollPane(textArea));
        this.frame.add(this.remark);

        this.frame.setSize(640,200);
        this.frame.setVisible(true);
        this.frame.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent e){
                System.exit(1);
            }
        });
        this.open.addActionListener(this);//为保存按钮添加事件监听
        this.save.addActionListener(this);//为保存按钮添加事件监听
    }

    public void actionPerformed(ActionEvent e){
        int result;   //接收操作状态
        JFileChooser fileChooser = new JFileChooser();//创建文件选择框
        if(e.getSource()==this.save){
            System.out.println("save file");
            try {
                Desktop.getDesktop().open((new File("doc/temp.xlsx")));
            } catch (IOException e1) {
                e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }else if(e.getSource()==this.open){//表示执行的是打开文件的操作
            this.textArea.setText("");//将文本域中的内容清空
            fileChooser.setApproveButtonText("确定");
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Excel File", "xlsx");

            fileChooser.setFileFilter(filter);
            result = fileChooser.showOpenDialog(this.frame);
            if(result==JFileChooser.APPROVE_OPTION){//选择的是确定按钮
                file = fileChooser.getSelectedFile();//得到选择的文件
                this.label.setText("打开的文件名称为：" + file.getAbsolutePath());
            }else if(result==JFileChooser.CANCEL_OPTION){//选择的是取消按钮
                this.label.setText("没有选择任何文件");
            }else{
                this.label.setText("操作出现错误");
            }
            if(file!=null){
                try
                {
                    Thread thread=new Thread(this);
                    thread.start();
                }
                catch (Exception e1)
                {
                    e1.printStackTrace();
                }
            }
        }
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
                    return malFormat.format(cell.getNumericCellValue());
                }
            case Cell.CELL_TYPE_BOOLEAN:
                return (cell.getBooleanCellValue());
            case Cell.CELL_TYPE_FORMULA:
                return (cell.getCellFormula());
            default:
                return null;
        }
    }



    @Override
    public void run() {
        try {
            this.jpBar.setValue(5);
            Translate translate= new Translate();
            FileInputStream fns = new FileInputStream(file);
            Workbook wb = WorkbookFactory.create(fns);
            Sheet sheet1 = wb.getSheetAt(0);
            int lastNum =  sheet1.getLastRowNum();
            int step ;
            if(lastNum>100){
                step =1;
            } else{
                step  = 100/lastNum;
            }
            this.jpBar.setValue(10);
           //FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();

            for(int i=2;i<lastNum;i++){
                //if (i>15)break;
                Row row = sheet1.getRow(i);
                Cell cell = row.getCell(3);
                Object obj = this.getCellValue(cell);
                if(obj!=null){
                    CellStyle cellStyle =cell.getCellStyle();
                    String name = getCellValue(row.getCell(2)).toString();
                    Cell phoneCell = row.getCell(5);
                    Cell mobileCell = row.getCell(6);
                    String phone = null;
                    String mobile = null;
                    if(phoneCell!=null){
                       Object o = getCellValue(phoneCell) ;
                       if(o!=null){
                           phone = o.toString();
                       }
                    }
                    if(mobileCell!=null){
                        Object o2  =  getCellValue(mobileCell);
                        if(o2!=null){
                            mobile = o2.toString();
                        }
                    }

                    if(phone==null){
                        phone = mobile;
                    }else{
                        phone = phone +", " + mobile;
                    }


                    //String nameEn ="";
                    AddressBean address = CommonUtils.parseAddress(obj);
                   // String shippingCn =  StringUtils.substring(address.getShippingCn(),0,10);
                    String shippingCn =  address.getShippingCn();
                    //System.out.println("shippingCn["+shippingCn+"]");

                    JSONObject english = translate.translate(shippingCn +"\n"+name);

                   // JSONObject english = translate.translate(name);

                    if(english!=null){
                        if(english.has("error_code"))  {
                            this.remark.setText("<html><body><div>"+english.getString("error_msg")+"</div><div>"+english.getString("error_code")+"</div></body></html>");
                            continue;
                        }

                        AddressBean entity = CommonUtils.parseAddressEn(english);
                        Cell cell25 = row.createCell(25);
                        cell25.setCellStyle(cellStyle);
                        cell25.setCellValue(entity.getPostCode());

                         Cell cell24 = row.createCell(24);
                        cell24.setCellValue(entity.getAddress1());
                        cell24.setCellStyle(cellStyle);

                        Cell cell23 = row.createCell(23);
                        cell23.setCellValue(entity.getAddress2());
                        cell23.setCellStyle(cellStyle);

                        Cell cell22 =row.createCell(22);
                        cell22.setCellValue(entity.getAddress3());
                        cell22.setCellStyle(cellStyle);

                        String shipping = StringUtils.join(new String[]{entity.getNameEn(),phone,entity.getShippingEn()},",") ;
                        if(StringUtils.isNotBlank(address.getZipCode())){
                            shipping+=address.getZipCode();
                        }
                        Cell cell27 =row.createCell(27) ;
                        cell27.setCellValue(shipping);
                        cell27.setCellStyle(cellStyle);

                        Cell cell20 = row.createCell(20);
                        cell20.setCellValue(entity.getNameEn());
                        cell20.setCellStyle(cellStyle);


                        this.remark.setText("<html><body><div>"+address.getShippingCn()+"</div><div>"+entity.getShippingEn()+"</div></body></html>");
                       /* Cell nameEnCell =  row.getCell(20);
                        if(nameEnCell == null){
                            nameEnCell =  row.createCell(20);
                        }
                        nameEnCell.setCellType(Cell.CELL_TYPE_STRING);
                        nameEnCell.setCellValue(nameEn);*/
                        //row.setRowStyle(cell.getCellStyle());
                     /*   else{
                            if(nameEnCell.getCellType()==Cell.CELL_TYPE_FORMULA){
                                //cell.setCellType(Cell.CELL_TYPE_STRING);
                                //nameEnCell.setCellFormula(nameEnCell.getCellFormula());
                            }else{
                                nameEnCell.setCellValue(nameEn);
                            }
                        }*/
                    }
                    if(step<100){
                        this.jpBar.setValue(++step);
                    }
                }

             /*   try{
                    Thread.sleep(1000);
                }catch (Exception e){

                }*/

            }

            File out = File.createTempFile("Riky",".xlsx");
            FileOutputStream fos = new FileOutputStream(out);
            wb.write(fos);
            fos.flush();
            fos.close();
            fns.close();
            if(step<100){
                this.jpBar.setValue(100);
            }
            Desktop.getDesktop().open(out);
            file = null;
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}

public class JFileChooserDemo{

    public static void main(String args[]){
        new Note();
    }
}