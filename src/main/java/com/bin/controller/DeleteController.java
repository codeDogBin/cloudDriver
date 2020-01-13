package com.bin.controller;

import com.bin.dao.FolderDao;
import com.bin.domain.Fil;
import com.bin.domain.Folder;
import com.bin.service.FilService;
import com.bin.service.FolderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.sql.Timestamp;
import java.util.List;

@Component
public class DeleteController {
    @Autowired
    private FolderService folderService;
    @Autowired
    private FilService filService;
    //每天早上凌晨5点触发发该任务
    @Scheduled(cron = "0 5 0 * * *")
    //30天后自动删除30天前的过期文件和数据库中的内容
    public void taskCycle(){
        //获取时
        Timestamp timestamp = new Timestamp(System.currentTimeMillis()-30*24*3600*1000);
        List<Folder> expireFol = folderService.getExpireFol(timestamp);
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
}
