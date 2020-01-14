package com.bin.service;

import com.bin.dao.User_Company_Dao;
import com.bin.domain.User_Company;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service("user_company_service")
public class User_Company_Service {
    @Autowired
    private User_Company_Dao user_company_dao;
    /*
     * 功能描述 新增用户和公司关联
     * @Author bin
     * @param user_company 
     * @return boolean        
     */
    public boolean insertUserCompany(User_Company user_company){
        Integer x = user_company_dao.insert(user_company);
        return x==1?true:false;
    }
    /*
     * 功能描述 查询用户和公司的关联表
     * @Author bin
     * @param userid 
     * @return java.util.List<com.bin.domain.User_Company>        
     */
    public List<User_Company> selectCompanyByUserId(int userid){
        return user_company_dao.selectCompanyByUserId(userid);
    }

    public void delUserCompany(User_Company user_company){
        user_company_dao.delete(user_company);
    }

    public Integer getCompanyCountByUserID(int user_id){
        return user_company_dao.getCompanyCountByUserID(user_id);
    }
}
