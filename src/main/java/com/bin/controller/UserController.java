package com.bin.controller;

import com.bin.domain.Company;
import com.bin.domain.Permission;
import com.bin.domain.User;
import com.bin.service.CompanyService;
import com.bin.service.PermissionService;
import com.bin.service.UserService;
import com.bin.service.User_Company_Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private User_Company_Service user_company_service;
    @Autowired
    private CompanyService companyService;
    @Autowired
    private PermissionService permissionService;

    @RequestMapping("/toLogin")
    public String toLogin(){
        return "login";
    }

    @RequestMapping("/toRegister")
    public  String toRegister(){
        return "register";
    }
    /*
     * 功能描述 登录
     * @Author bin
     * @param name
     * @param password
     * @param request 
     * @return java.lang.String        
     */
    @RequestMapping("/login.do")
    public String login(String name, String password, HttpServletRequest request){
        User user = userService.login(name, password);
        if(user ==null){
            request.setAttribute("msg","登陆失败");
            return "login";
        }
        Permission permission = permissionService.findPermission(user.getPermission_id());
        request.getSession().setAttribute("permission",permission);
        request.getSession().setAttribute("user",user);
        return "forward:index.do";
    }
    /*
     * 功能描述 前往主页 并且将公司信息存入requester
     * @Author bin
     * @param request 
     * @return java.lang.String        
     */
    @RequestMapping("/index.do")
    public String index(HttpServletRequest request){
        User user = (User)request.getSession().getAttribute("user");
        Permission permission = (Permission)request.getSession().getAttribute("permission");
        List<Company> companies;
        int sumPages ;
        if(permission.isReadall()){//如果当前权限是读取所有公司的权限
            companies = companyService.selectAll(0,20);
            Integer allCompanyCount = companyService.getAllCompanyCount();
            sumPages = (allCompanyCount+19)/20;
        }else{
            companies  = companyService.findCompanyByUserIDLimit(user.getId(),0);
            Integer companyCountByUserID = user_company_service.getCompanyCountByUserID(user.getId());
            sumPages = (companyCountByUserID+19)/20;
        }
        request.setAttribute("sumPages",sumPages);
        request.setAttribute("companies",companies);
        return "index";
    }
    @RequestMapping("/selectCompanyByNameIndex.do")
    public String selectIndex(String name,HttpServletRequest request){
        System.out.println();
        List<Company> companies;
        int sumPages ;
        companies = companyService.SelectByName(name,0);
        sumPages = (companyService.SelectCountByName(name)+19)/20;
        request.setAttribute("sumPages",sumPages);
        request.setAttribute("companies",companies);
        request.setAttribute("name",name);
        return "index";
    }
    @ResponseBody
    @RequestMapping("/indexAJAX.do")
    public List indexAJAX(int start,String name,HttpServletRequest request){
        User user = (User)request.getSession().getAttribute("user");
        Permission permission = (Permission)request.getSession().getAttribute("permission");
        List<Company> companies;
        if(permission.isReadall()){//如果当前权限是读取所有公司的权限
            if(name==null||"".equals(name)){
                companies = companyService.selectAll(start,20);
            }else {
                companies= companyService.SelectByName(name,start);
            }
        }else{
            companies  = companyService.findCompanyByUserIDLimit(user.getId(),start);
        }
//        System.out.println(companies);
        return companies;
    }

   
    /*
     * 功能描述 注册
     * @Author bin
     * @param name
    * @param password
    * @param permission_id
    * @param request
     * @return java.lang.String        
     */
    @RequestMapping("/register.do")
    public String register(String name,
                           String password,
                           int permission_id,
                           HttpServletRequest request){
        User user = new User();
        user.setName(name);
        user.setPassword(password);
        user.setPermission_id(permission_id);
        boolean register = userService.register(user);
        if(register){
            request.setAttribute("msg","注册成功");
            return "index";
        }
        request.setAttribute("msg","注册失败");
        return "register";
    }
    @ResponseBody
    @RequestMapping("findUserByNameAJAX.do")
    public List findUserByNameAJAX(String userName){
        System.out.println(userName);
        return userService.findByName(userName);
    }
}
