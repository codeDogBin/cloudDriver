package com.bin.util;

import java.io.File;
import java.util.UUID;
//创建文件夹工具类
public class CreateFolderUtil {
        //创建一个的文件夹
        public static String createFolder(String path) throws Exception {
        File file = createFile(path);
        while(file.exists()){
            file = createFile(path);
        }
        try {
            file.mkdir();
            return file.getPath();
        } catch (Exception e) {
            throw new Exception("文件夹创建失败");
        }
    }
    //创建文件的地址
    private static File createFile(String path){
        return new File(path+File.separator+UUID.randomUUID().toString().replaceAll("-",""));
    }
}
