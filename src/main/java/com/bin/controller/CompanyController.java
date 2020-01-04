package com.bin.controller;

import com.bin.domain.Company;
import com.bin.domain.Permission;
import com.bin.domain.User;
import com.bin.domain.User_Company;
import com.bin.service.CompanyService;
import com.bin.service.UserService;
import com.bin.service.User_Company_Service;
import com.bin.util.CreateCompanyFolderUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
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
    /*
     * 功能描述 创建公司
     * @Author bin
     * @param name
     * @param request
     * @return java.lang.String
     */
    @RequestMapping("/createCompany.do")
    public String createCompany(String name, HttpServletRequest request) throws Exception {
        Company company = new Company();
        try {
            company.setName(name);
            String way = CreateCompanyFolderUtil.createFloder();
            company.setWay(way);
            company.setCtime(new Timestamp(System.currentTimeMillis()));
            companyService.registerCompany(company);
            request.setAttribute("msg","创建成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "forward:index.do";
    }
    /*
     * 功能描述 前往公司页面
     * @Author bin
     * @param request
     * @return java.lang.String
     */
    @RequestMapping("/toCompany")
    public String toCompany(HttpServletRequest request){
        List<Company> companies = companyService.selectAll();
        List<User> users = userService.findAllCus();
        request.setAttribute("companies",companies);
        request.setAttribute("users",users);
        return "company";
    }
    /*
     * 功能描述 查询用户绑定和未绑定的id
     * @Author bin
     * @param user_id
     * @return java.util.List
     */
    @ResponseBody
    @RequestMapping("findUserCompanyAJAX.do")
    public List findUserCompany(int user_id){
        List<Company> userCompanies = companyService.findCompanyByUserID(user_id);
        List<Company> allCompanies = companyService.selectAll();
        allCompanies.removeAll(userCompanies);
//        System.out.println(userCompanies);
//        System.out.println(allCompanies);
        List list = new ArrayList();
        list.add(userCompanies);
        list.add(allCompanies);
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

    @ResponseBody
    @RequestMapping("/unbindUserCompanyAJAX.do")
    public String unbindUserCompany(@RequestParam("user_id") int user_id, @RequestParam("companies_id") int[] companies_id){
        System.out.println(Arrays.toString(companies_id));
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
}
