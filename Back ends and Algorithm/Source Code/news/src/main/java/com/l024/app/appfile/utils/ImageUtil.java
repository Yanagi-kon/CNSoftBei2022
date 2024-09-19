package com.l024.app.appfile.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Base64;

public class ImageUtil {

    /**
     * @Description: 将base64编码字符串转换为图片
     * @Author:
     * @CreateTime:
     * @param file base64编码字符串
     * @param path 图片路径-具体到文件
     * @return
     */
    public static String generateImage(String file, String path) {
        // 解密
        try {
            // 解密
            Base64.Decoder decoder = Base64.getDecoder();
            // 去掉base64前缀 data:image/jpeg;base64,
            //file = file.substring(file.indexOf(",", 1) + 1, file.length());
            byte[] b = decoder.decode(file.replaceAll(" ","+"));
            // 处理数据
            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {
                    b[i] += 256;
                }
            }
            // 保存图片
            OutputStream out = new FileOutputStream(path);
            out.write(b);
            out.flush();
            out.close();
            // 返回图片的相对路径 = 图片分类路径+图片名+图片后缀
            return path;
        } catch (IOException e) {
            return null;
        }
    }
    public static void delFile(String path) {
        File file = new File(path);
        delete(file);
    }

    public static void delete(File file) {
        if (!file.exists()) return;

        if (file.isFile() || file.list() == null) { //传的是文件
            file.delete();
            System.out.println("删除了" + file.getName());
        } else { //传文件夹里有子文件
            File[] files = file.listFiles();
            for (File a : files) {
                delete(a);        //回调
            }
            file.delete();
            System.out.println("删除了" + file.getName());
        }
    }
}
