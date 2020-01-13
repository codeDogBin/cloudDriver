package com.bin.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipUtil {
    /**
     * 将文件压缩成zip文件
     * @param filePaths 一个文件集合 每个文件包含他的绝对文件地址和逻辑文件地址
     * @param zos 文件压缩流
     * @throws IOException
     */
    public static void zipFile( List<String[]> filePaths, ZipOutputStream zos) throws IOException {
        //循环读取文件路径集合，获取每一个文件的路径
        for(String filePath[] : filePaths) {
            File inputFile = new File(filePath[1]);  //根据文件路径创建文件
            if (inputFile.exists()) { //判断文件是否存在
                if (inputFile.isFile()) {  //判断是否属于文件，还是文件夹
                    //创建输入流读取文件（使用绝对地文件地址）
                    BufferedInputStream bis = new BufferedInputStream(new FileInputStream(inputFile));
                    //将文件写入zip内，即将文件进行打包（使用多级文件地址）
                    zos.putNextEntry(new ZipEntry(filePath[0]));
                    //写入文件的方法
                    int size = 0;
                    byte[] buffer = new byte[1024];  //设置读取数据缓存大小
                    while ((size = bis.read(buffer)) > 0) {
                        zos.write(buffer, 0, size);
                    }
                    //关闭输入输出流
                    zos.closeEntry();
                    bis.close();
                }
            }
        }
    }
}
