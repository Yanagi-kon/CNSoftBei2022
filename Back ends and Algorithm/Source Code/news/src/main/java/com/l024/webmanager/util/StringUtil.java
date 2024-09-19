package com.l024.webmanager.util;

public class StringUtil {

    public static boolean isEmpty(String str){
        if(str!=null&&str.trim().length()>0){
            return false;
        }
        return true;
    }
}
