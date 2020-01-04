package com.bin.service;

import com.bin.dao.FolderDao;
import com.bin.domain.Fil;
import com.bin.domain.Folder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("folderService")
public class FolderService {
    @Autowired
    private FolderDao folderDao;
    /*
     * 功能描述 新增文件夹
     * @Author bin
     * @param folder
     * @return boolean
     */
    public boolean insertFolder(Folder folder){
        Integer x = folderDao.insert(folder);
        return x==1?true:false;
    }
    /*
     * 功能描述 查询文件夹 通过文件夹父id和公司id
     * @Author bin
     * @param fway_id
     * @param company_id
     * @return java.util.List<com.bin.domain.Folder>
     */
    public List<Folder> findByFidCid(int fway_id,int company_id){
        return folderDao.findByFidCid(fway_id,company_id);
    }
     /*
      * 功能描述 通过父文件夹id找父文件夹
      * @Author bin
      * @param fway_id 
      * @return com.bin.domain.Folder        
      */   
    public Folder findByFidAsId(int fway_id){
        return folderDao.findByFidAsId(fway_id);
    }
    /*
     * 功能描述 将文件夹设置为过期
     * @Author bin
     * @param fil_id 
     * @return void        
     */
    public void expireFolById(int fil_id){
        folderDao.expireFolById(fil_id);
    }
    
    public void delFolByID(int id){
        folderDao.delFolById(id);
    }
    /*
     * 功能描述 查找所有过期文件夹
     * @Author bin
     * @param  
     * @return java.util.List<com.bin.domain.Folder>        
     */
    public List<Folder> allExpire(){
       return folderDao.allExpire();
    }
}
