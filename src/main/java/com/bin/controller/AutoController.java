package com.bin.controller;

import com.bin.domain.Fil;
import com.bin.domain.Folder;
import com.bin.service.FilService;
import com.bin.service.FolderService;
import com.bin.util.TimeUtil;
import com.bin.util.ZipUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;
import java.util.zip.ZipOutputStream;

/*
 * 功能描述 系统定时自动任务
 * @Author bin
 * @param null
 * @return
 */
@Component
public class AutoController {
    @Autowired
    private FolderService folderService;
    @Autowired
    private FilService filService;
    //表示每个月1日凌晨2点执行该任务
    @Scheduled(cron = "0 0 2 1 * ? ")
    public void autoZip() throws IOException {
        String[] lastTime = TimeUtil.getLastMonth();
        String yearMonth =lastTime[0];
        //设置时间戳
        Timestamp startTime = new Timestamp(TimeUtil.getLastTime().getTime());
        Timestamp endTime = new Timestamp(TimeUtil.getThisTime().getTime());
        //找到最近一个月内的所有文件
        List<Fil> fils = filService.getFilByMonth(startTime,endTime);
        //设置存储文件夹ID和对应路径的Map
        Map<Integer,String> wayMap=new HashMap();
        //将所有文件夹id和对应路径存入
        folderService.allContent(wayMap);
        //将所有文件的名字和文件存好
        List<String[]> filNameAndWay = filNameAndWay(fils,wayMap);
        //设置存储地址
        String zipBasePath = "D:/couldriver";
        String zipName = yearMonth+"新增客户文件.zip";
        String zipFilePath = "";
        //将压缩文件的地址拼接并且创建好
        for (int i = 1; i < 3; i++) {
            zipFilePath= zipBasePath +File.separator+lastTime[i];
            File zip = new File(zipFilePath);
            if (!zip.exists()) {
                zip.mkdir();
            }
        }
        zipFilePath = zipFilePath +File.separator + zipName;
        //压缩文件的-创建
        File zip = new File(zipFilePath);
        if (!zip.exists()) {
            zip.createNewFile();
        }
        try {
            ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zip));
            ZipUtil.zipFile(filNameAndWay, zos); //调用util工具创建
            zos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //每天早上凌晨5点触发发该任务
    @Scheduled(cron = "0 0 5 * * *")
    //自动删除30天前的过期文件和数据库中的内容
    public void autoDelete(){
        //获取时间
        Timestamp timestamp = new Timestamp(System.currentTimeMillis()-30*24*3600*1000l);
        //获取过期文件夹
        List<Folder> expireFol = folderService.getExpireFol(timestamp);
        //获取过期文件
        List<Fil> expireFil = filService.getExpireFil(timestamp);
        for (Fil fil : expireFil) {
            File file = new File(fil.getWay());
            file.delete();
            filService.delFilByID(fil.getId());
        }
        for (Folder folder : expireFol) {
            File file = new File(folder.getWay());
            deleteFolder(file);
            //删除数据库中文件夹下的内容
            folderService.delFolByID(folder.getId());
        }
    }

    //删除文件夹的方法
    public static void deleteFolder(File file){
        if(file.isDirectory()) {
            String[] list = file.list();
            for (int i = 0; i < list.length; i++) {
                deleteFolder(new File(file,list[i]));
            }
        }
        file.delete();
    }

    /*
     * 功能描述 filNameAndWay 获取文件的全路径名和名称
     * @Author bin
     * @param fils 文件集合
     * @param wayMap 路径集合
     * @return java.util.List<java.lang.String[]>
     */
    private static List<String[]> filNameAndWay(List<Fil> fils,Map<Integer,String> wayMap){
        List <String[]> filNameAndWay = new ArrayList<>();
        for (Fil fil : fils) {
            //如果父文件夹为null，则跳过
            if(wayMap.get(fil.getFol_id())==null)
                continue;
            filNameAndWay.add(new String[]{wayMap.get(fil.getFol_id())+fil.getName(),fil.getWay()});
        }
        return filNameAndWay;
    }
}
