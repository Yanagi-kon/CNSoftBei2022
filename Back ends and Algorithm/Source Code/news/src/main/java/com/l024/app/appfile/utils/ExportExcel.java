package com.l024.app.appfile.utils;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class ExportExcel {
    /**
     * @param sheetName 工作表的名字
     * @param column  列名
     * @param data  需要导出的数据    ( map的键定义为列的名字 一定要和column中的列名保持一致  )
     */
    public static void exportExcel(String sheetName, List<String> column, List<Map<String,Object>> data) throws IOException {
        //创建工作薄
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
        //创建sheet
        HSSFSheet sheet = hssfWorkbook.createSheet(sheetName);
        // 表头
        HSSFRow headRow = sheet.createRow(0);
        for (int i = 0; i < column.size(); i++){
            headRow.createCell(i).setCellValue(column.get(i));
        }

        for (int i = 0; i < data.size(); i++) {
            HSSFRow dataRow = sheet.createRow(sheet.getLastRowNum() + 1);
            for (int x = 0; x < column.size(); x++) {
                try{
                    dataRow.createCell(x).setCellValue(data.get(i).get(column.get(x))==null?"":data.get(i).get(column.get(x)).toString());
                }catch(Exception e){
                    break;
                }

            }
        }
        File file=new File("D:\\IdealProject\\news\\"+sheetName);
        FileOutputStream output= new FileOutputStream(file,false);
        hssfWorkbook.write(output);
        output.flush();

    }



}
