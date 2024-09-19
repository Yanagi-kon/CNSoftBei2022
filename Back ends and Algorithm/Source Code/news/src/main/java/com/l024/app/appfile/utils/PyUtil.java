package com.l024.app.appfile.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class PyUtil {
    public static String GetPyFile(String pyPath) {
        String result="";
        try {
            //String[] args = new String[]{"python", pyPath};
            String args=pyPath;
            Process proc = Runtime.getRuntime().exec(args);// 执行py文件
            BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            String line = null;
            while ((line = in.readLine()) != null) {
                System.out.println(line);
                result=result+line+" ";
            }
            in.close();
            proc.waitFor();
        } catch (
                IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }
}
