package com.l024.webmanager.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogUtil{
    private static Class cla;
    private static Logger logger;

    public static void setCla(Class cla) {
        LogUtil.cla = cla;
        logger = LoggerFactory.getLogger(cla);
    }

    public static void i(String key,Object msg){
        print(key,msg);
    }

    private static void print(String key,Object msg){
        if(cla==null){
            cla = LogUtil.class;
        }
        if(logger==null){
            logger = LoggerFactory.getLogger(cla);
        }
        logger.info(key+": [{}]",msg);
    }
}
