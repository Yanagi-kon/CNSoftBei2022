package com.l024.webmanager.util;

import java.lang.reflect.Field;

/**
 * @Auther: Mr.yang
 * @Date: 2022/06/05/1:27
 * @Description: 描述：
 */
public class ObjUtils {
    public static boolean isNotNull(Object object){
        boolean flag = true;

        Class clazz = object.getClass();
        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            //设置属性是可以访问的(私有的也可以)
            field.setAccessible(true);
            Object value = null;
            try {
                value = field.get(object);
                // 只要有1个属性不为空,那么就不是所有的属性值都为空
                if (value != null) {
                    flag = false;
                    break;
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return flag;


    }
}
