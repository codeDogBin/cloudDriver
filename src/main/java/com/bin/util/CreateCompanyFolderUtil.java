package com.bin.util;

import java.io.File;
import java.util.UUID;

public class CreateCompanyFolderUtil {
    private final static String rootPath ="D:\\couldriver";
    public static String createFloder() throws Exception {
        File file = createFile();
        while(file.exists()){
            file = createFile();
        }
        try {
            file.mkdir();
            return file.getPath();
        } catch (Exception e) {
            throw new Exception("文件夹创建失败");
        }
    }
    private static File createFile(){
        return new File(rootPath +File.separator+ UUID.randomUUID().toString().replaceAll("-", "")+File.separator);
    }

}
