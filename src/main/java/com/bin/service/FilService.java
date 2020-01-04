package com.bin.service;

import com.bin.dao.FilDao;
import com.bin.domain.Fil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("filService")
public class FilService {
    @Autowired
    private FilDao filDao;
    /*
     * 功能描述  新增一个文件
     * @Author bin
     * @param fil 
     * @return boolean        
     */
    public boolean insertFil(Fil fil){
        Integer x = filDao.insert(fil);
        return x==1?true:false;
    }
    /*
     * 功能描述  通过文件夹id找到当前文件夹下的文件
     * @Author bin
     * @param fol_id
     * @return java.util.List<com.bin.domain.Fil>
     */
    public List<Fil> FindByFid(int fol_id){
       return filDao.findByFid(fol_id);
    }
    /*
     * 功能描述 文件设置为过期
     * @Author bin
     * @param fil_id 
     * @return void        
     */
    public void expireFilById(int fil_id){
        filDao.expireFileById(fil_id);
    }  
    /*
     * 功能描述 查询所有过期文件
     * @Author bin
     * @param  
     * @return java.util.List<com.bin.domain.Fil>        
     */
    public List<Fil> allExpire(){
       return filDao.allExpire();
    }
    /*
     * 功能描述 删除文件ByID
     * @Author bin
     * @param null
     * @return
     */
    public void delFilByID(int id){
        filDao.delFilById(id);
    }
}