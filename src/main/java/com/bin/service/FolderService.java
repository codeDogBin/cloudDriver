package com.bin.service;

import com.bin.dao.CompanyDao;
import com.bin.dao.FilDao;
import com.bin.dao.FolderDao;
import com.bin.domain.Company;
import com.bin.domain.Fil;
import com.bin.domain.Folder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.io.File;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

@Service("folderService")
public class FolderService {
    @Autowired
    private FolderDao folderDao;
    @Autowired
    private FilDao filDao;
    @Autowired
    private CompanyDao companyDao;
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
    @Transactional
    public void delFolByID(int id){
        //找到所有的子文件夹
        List<Folder> folders = folderDao.findByFid(id);
        //继续遍历子文件夹
        for (Folder folder : folders) {
            delFolByID(folder.getId());
        }
        List<Fil> fils = filDao.findByFid(id);
        for (Fil fil : fils) {
            filDao.delFilById(fil.getId());
        }
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

    public List<Folder> allFolder(){
        return folderDao.allFolder();
    }
    public Folder chongMing(Folder folder) {
        Folder temp;
        temp = folderDao.findByNameFidCidState(folder);
        while(temp != null){
            folder.setName(folder.getName()+"_副本");
            temp = folderDao.findByNameFidCidState(folder);
        }
        return folder;
    }

    public Folder findByNameFidCid(String name,int fway_id,int company_id){
       return folderDao.findByNameFidCid(name,fway_id,company_id);
    }

    public Folder findExpireByID(int fol_id){
        return folderDao.findExpireByID(fol_id);
    }
    public Folder findByID(int fol_id){
        return folderDao.findByID(fol_id);
    }

    public List<Folder> getExpireFol(Timestamp timestamp){
        return folderDao.getExpireFol(timestamp);
    }

    public void renameFol(String name,int folder_id){
        folderDao.renameFol(name,folder_id);
    }

    public void expireFol(Folder folder) {
        folderDao.expireFol(folder);
    }

    public void recoverFol(Folder fol) {
        folderDao.recoverFol(fol);
    }

    public void allContent(Map<Integer,String> wayMap){
        List<Folder> folders = folderDao.allFolder();
        for (Folder folder: folders){
            StringBuilder way = new StringBuilder();
            contents(folder,wayMap,way);
        }
    }
    private  void contents(Folder folder,Map<Integer,String> wayMap,StringBuilder way){
        //判断父级是不是公司
        int fway_id=folder.getFway_id();
        //递归阶段
        if(fway_id==0){
            Company company = companyDao.findCompanyById(folder.getCompany_id());
            way.append(company.getName()).append(File.separator).append(folder.getName()).append(File.separator);
            wayMap.put(folder.getId(),way.toString());
        }else{//如果不是公司 尝试去数据库中查找地址
            String s = wayMap.get(fway_id);
            //判断地址是否为空 如果为空，继续递归
            if ("".equals(s)||s==null){
                Folder tempFolder = folderDao.findByFidAsId(fway_id);
                contents(tempFolder, wayMap, way);
                way.append(folder.getName()).append(File.separator);
            }
            //如果不为空，将父级地址拼接上本级地址
            else way= new StringBuilder(s).append(folder.getName()).append(File.separator);
        }
        wayMap.put(folder.getId(),way.toString());
    }
}
