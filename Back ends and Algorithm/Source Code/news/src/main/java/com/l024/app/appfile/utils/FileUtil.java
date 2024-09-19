package com.l024.app.appfile.utils;

import java.util.HashMap;
import java.util.Random;

/**
 * @Auther: Mr.yang
 * @Date: 2022/06/04/17:15
 * @Description: 描述：
 */
public class FileUtil {
    public static String filetoName(){
        long l = System.currentTimeMillis();
        Random random = new Random();
        String i = null;
        for (int j = 0; j <5; j++) {
            i = String.valueOf(random.nextInt(10));
        }
        return l+i;
    }

    public static void main(String[] args) {
        HashMap<String, String> stringStringHashMap = new HashMap<>();
        stringStringHashMap.put("a","100");
        stringStringHashMap.put("b","100");
        stringStringHashMap.put("a","100");

    }
}
