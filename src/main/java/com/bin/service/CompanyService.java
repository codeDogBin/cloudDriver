package com.bin.service;

import com.bin.dao.CompanyDao;
import com.bin.domain.Company;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("companyService")
public class CompanyService {
    @Autowired
    private CompanyDao  companyDao;
    /*
     * 功能描述 注册公司
     * @Author bin
     * @param company
     * @return boolean
     */
    public boolean registerCompany(Company company){
        Integer x = companyDao.insertCompany(company);
        return  x==1?true:false;
    }
    /*
     * 功能描述 通过公司id查询公司
     * @Author bin
     * @param companyId 
     * @return com.bin.domain.Company        
     */
    public Company selectCompany(int companyId){
        return companyDao.findCompanyById(companyId);
    }
    /*
     * 功能描述 通过用户id查询公司
     * @Author bin
     * @param user_id 
     * @return java.util.List<com.bin.domain.Company>        
     */
    public List<Company> findCompanyByUserID(int user_id){
        return  companyDao.findCompanyByUserID(user_id);
    }
    /*
     * 功能描述 查询所有公司
     * @Author bin
     * @param  
     * @return java.util.List<com.bin.domain.Company>        
     */
    public List<Company> selectAll(){
        return companyDao.selectAll();
    }
    /*
     * 功能描述 通过公司名称查找公司
     * @Author bin
     * @param companyName
     * @return java.util.List<com.bin.domain.Company>
     */
    public List<Company> SelectByName(String companyName){
        companyName="%"+companyName+"%";
        return companyDao.selectByName(companyName);
    }

}
