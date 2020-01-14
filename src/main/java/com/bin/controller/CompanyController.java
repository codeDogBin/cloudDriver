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
    @RequestMapping("/toCompany")
    public String toCompany(HttpServletRequest request){
        //查询所有用户
        List<User> users = userService.findAllCus();
        //将用户存入
        request.setAttribute("users",users);
        return "company";
    }

    /*
     * 功能描述 创建公司
     * @Author bin
     * @param name
     * @param request
     * @return java.lang.String
     */
    @RequestMapping("/createCompany.do")
    public String createCompany(String name, HttpServletRequest request) throws Exception {
        //判断name是否为空
        if(name==null||"".equals(name)){
            request.setAttribute("msg","公司名不能为空");
            return "forward:index.do";
        }
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
            request.setAttribute("msg","创建成功");

        } catch (Exception e) {
            request.setAttribute("msg","创建失败，检查是否有相同名称的公司");
            e.printStackTrace();
        }
        return "forward:index.do";
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
        List list = new ArrayList();
        list.add(userCompanies);
        return list;
    }

    /*
     * 功能描述 绑定用户和公司
     * @Author bin
     * @param user_id
     * @param company_id
     * @return void
     */
    @ResponseBody
    @RequestMapping("/bindUserCompanyAJAX.do")
    public String bindUserCompany(@RequestParam("user_id") int user_id, @RequestParam("companies_id") int[] companies_id){
        try {
            for (int i = 0; i < companies_id.length; i++) {
                User_Company user_company= new User_Company();
                user_company.setUser_id(user_id);
                user_company.setCompany_id(companies_id[i]);
                user_company.setCtime(new Timestamp(System.currentTimeMillis()));
                user_company_service.insertUserCompany(user_company);
            }
        } catch (Exception e) {
            return "添加失败";
        }
        return "添加成功";
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
    public String unbindUserCompany(@RequestParam("user_id") int user_id, @RequestParam("companies_id") int[] companies_id){
        try {
            for (int i = 0; i < companies_id.length; i++) {
                User_Company user_company= new User_Company();
                user_company.setUser_id(user_id);
                user_company.setCompany_id(companies_id[i]);
                user_company.setCtime(new Timestamp(System.currentTimeMillis()));
                user_company_service.delUserCompany(user_company);
            }
        } catch (Exception e) {
            return "解绑失败";
        }
        return "解绑成功";
    }
    /*
     * 功能描述 findCompanyByName
     * @Author bin
     * @param name
     * @param user_id
     * @return java.util.List<com.bin.domain.Company>
     */
    @ResponseBody
    @RequestMapping("findCompanyByNameAJAX.do")
    public List<Company> findCompanyByName(String name, int user_id){
        List<Company> companies = companyService.SelectByNameNoPage(name);
        List<Company> companyByUserID = companyService.findCompanyByUserID(user_id);
        companies.removeAll(companyByUserID);
        return companies;
    }

    @RequestMapping("")
    public boolean companyName(String name){
        Company c = companyService.findByName(name);
        if(c==null){
            return true;
        }
        return false;
    }
}
