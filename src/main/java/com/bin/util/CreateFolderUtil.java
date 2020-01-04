package com.bin.util;

import java.io.File;
import java.util.UUID;

public class CreateFolderUtil {
        public static String createFloder(String path) throws Exception {
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
    private static File createFile(String path){
        return new File(path+File.separator+UUID.randomUUID().toString().replaceAll("-",""));
    }
}