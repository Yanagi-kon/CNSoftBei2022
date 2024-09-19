package com.l024.app.appfile.utils;

import com.monitorjbl.xlsx.StreamingReader;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class ExcelRead {
    public static List<Map<String,String>> ReadExcel(File file) throws Exception{
        List<Map<String,String>> textList=new ArrayList<>();
        FileInputStream in = new FileInputStream(file);
        Workbook wk = StreamingReader.builder()
                .rowCacheSize(100)  //缓存到内存中的行数，默认是10
                .bufferSize(4096)  //读取资源时，缓存到内存的字节大小，默认是1024
                .open(in);  //打开资源，必须，可以是InputStream或者是File，注意：只能打开XLSX格式的文件
        int sheetNums = wk.getNumberOfSheets();
        for(int i = 0 ; i < sheetNums;i ++){
            List<String> sheetData = new ArrayList<String>();
            Sheet sheet = wk.getSheetAt(i);
            String sheetName = wk.getSheetName(i);

            //遍历所有的行
            int k = 0;

            for (Row row : sheet) {
                k++;
                int l=0;
                //遍历所有的列
                String title=null;
                String text=null;
                HashMap<String,String> hashMap = new HashMap<>();
                for (Cell cell : row) {
                    l++;
                    if(cell.getColumnIndex()==1){
                        title= cell.getStringCellValue();
                    }
                    if(title!=null && cell.getColumnIndex()==9){
                        text= cell.getStringCellValue();
                    }

                }
                hashMap.put(title, text);
                textList.add(hashMap);
            }

        }
        //注意第一行是表格各列标题
        return textList;
    }


}
