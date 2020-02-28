package com.bin.controller;

import com.bin.domain.Company;
import com.bin.domain.Folder;
import com.bin.domain.User;
import com.bin.domain.User_Company;
import com.bin.service.CompanyService;
import com.bin.service.FolderService;
import com.bin.service.UserService;
import com.bin.service.User_Company_Service;
import com.bin.util.CreateCompanyFolderUtil;
import com.bin.util.CreateFolderUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.*;

@Controller
public class CompanyController {
    @Autowired
    private UserService userService;
    @Autowired
    private CompanyService companyService;
    @Autowired
    private User_Company_Service user_company_service;
    @Autowired
    private FolderService folderService;
    private String[] folderName = new String[]{"证照","文档","财报"};
    /*
     * 功能描述 前往公司页面
     * @Author bin
     * @param request
     * @return java.lang.String
     */
    @ResponseBody
    @RequestMapping("/toCompany.do")
    public List<User> toCompany(HttpServletRequest request){
        //查询所有用户
        List<User> users = userService.findAllCus();
        //将用户存入
        request.setAttribute("users",users);
        return users;
    }
    /*
     * 功能描述 检查公司名是否占用
     * @Author bin
     * @param name 
     * @return java.lang.String        
     */
    @ResponseBody
    @RequestMapping("examineCompanyNameAJAX.do")
    public String examineCompanyNameAJAX(String name){
        Company company = companyService.findByName(name);
        if(company == null){
            return "OK";
        }
        return "FAIL";
    }

    /*
     * 功能描述 创建公司
     * @Author bin
     * @param name
     * @param request
     * @return java.lang.String
     */
    @ResponseBody
    @RequestMapping("/createCompanyAJAX.do")
    public String createCompanyAJAX(String name)  {
        //创建新公司对象
        Company company = new Company();

        try {
            //创建公司
            company.setName(name);
            String way = CreateCompanyFolderUtil.createFloder();
            company.setWay(way);
            company.setCtime(new Timestamp(System.currentTimeMillis()));
            //数据库新增
            companyService.registerCompany(company);
            //创建文件夹 证照和文件
            for (int i = 0; i < folderName.length; i++) {
                String folderWay = CreateFolderUtil.createFolder(way);
                Folder folder = new Folder();
                folder.setCompany_id(company.getId());
                folder.setName(folderName[i]);
                folder.setFway_id(0);
                folder.setWay(folderWay);
                folder.setCtime(new Timestamp(System.currentTimeMillis()));
                folderService.insertFolder(folder);
            }
        } catch (Exception e) {
            return "FAIL";
        }
        return "OK";
    }

    /*
     * 功能描述 查询用户绑定的公司
     * @Author bin
     * @param user_id
     * @return java.util.List
     */
    @ResponseBody
    @RequestMapping("/findUserCompanyAJAX.do")
    public List findUserCompany(int user_id){
        List<Company> userCompanies = companyService.findCompanyByUserID(user_id);
        return userCompanies;
    }
    /*
     * 功能描述 查询未绑定的的公司
     * @Author bin
     * @param name
     * @param user_id
     * @return java.util.List<com.bin.domain.Company>
     */
    @ResponseBody
    @RequestMapping("findUnbindCompanyByNameAJAX.do")
    public List<Company> findUnbindCompanyByName(String name, int user_id){
        List<Company> companies = companyService.SelectByNameNoPage(name);
        List<Company> companyByUserID = companyService.findCompanyByUserID(user_id);
        companies.removeAll(companyByUserID);
        return companies;
    }

    /*
     * 功能描述 绑定用户和公司
     * @Author bin
     * @param user_id
     * @param company_id
     * @return voiz
     * 单个绑定
     */
    @ResponseBody
    @RequestMapping("/bindUserCompanyAJAX.do")
    public String bindUserCompany(int user_id, int [] companies_id){
        try {
            for (int company_id : companies_id) {
                User_Company user_company= new User_Company();
                user_company.setUser_id(user_id);
                user_company.setCompany_id(company_id);
                user_company.setCtime(new Timestamp(System.currentTimeMillis()));
                user_company_service.insertUserCompany(user_company);
            }
        } catch (Exception e) {
            return "FAIL";
        }
        return "OK";
    }
    /*
     * 功能描述 unbindUserCompany 解绑用户和公司
     * @Author bin
     * @param user_id
     * @param companies_id
     * @return java.lang.String
     */
    @ResponseBody
    @RequestMapping("/unbindUserCompanyAJAX.do")
    public String unbindUserCompany( int user_id, int[] companies_id){
        try {
            for (int company_id : companies_id) {
                User_Company user_company= new User_Company();
                user_company.setUser_id(user_id);
                user_company.setCompany_id(company_id);
                user_company_service.delUserCompany(user_company);
            }

        } catch (Exception e) {
            return "FAIL";
        }
        return "OK";
    }
}
