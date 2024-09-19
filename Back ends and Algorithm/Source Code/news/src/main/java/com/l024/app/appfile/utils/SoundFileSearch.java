package com.l024.app.appfile.utils;
import java.io.File;

public class SoundFileSearch {
    public static Boolean SoundSearch(String path) {
        File dir=new File("D:\\SoundFile");      //确定要查找的目录dir;
        boolean flag=false;                              //flag来确定是否查到文件;
        return function(path,dir,flag);                     //function函数递归实现;

    }
    static Boolean function(String filename,File dir,boolean flag)
    {

        File[]files=dir.listFiles();
        for(File file:files)
        {
            if(file.isDirectory())                      //file是目录时，则重新调用function函数;
            {
                function(filename,file.getAbsoluteFile(),flag);
            }
            if(file.isFile() && filename.equals(file.getName()))  //file时文件且文件名相同时，输出;
            {
                flag=true;
                //System.out.println("要查找的文件路径为："+file.getAbsolutePath());
                break;
            }
        }
        if(flag==true)                                 //判断有没有找到过文件;
            return true;
        else
        {
            //System.out.print(dir.getName()+"文件夹下没有找到此文件");
            return false;
        }
    }



}
