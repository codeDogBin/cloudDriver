package com.bin.controller;

import com.bin.domain.Company;
import com.bin.domain.Permission;
import com.bin.domain.User;
import com.bin.domain.User_Company;
import com.bin.service.CompanyService;
import com.bin.service.PermissionService;
import com.bin.service.UserService;
import com.bin.service.User_Company_Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
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

    @RequestMapping("/toIndex")
    public String toIndex(){
        return "index";
    }
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
     * 功能描述 前往主页 并且将公司信息存入session
     * @Author bin
     * @param request 
     * @return java.lang.String        
     */
    @RequestMapping("/index.do")
    public String index(HttpServletRequest request){
        User user = (User)request.getSession().getAttribute("user");
        Permission permission = (Permission)request.getSession().getAttribute("permission");
        List<Company> companies= new ArrayList();
        if(permission.isReadall()){//如果当前权限是读取所有公司的权限
            companies = companyService.selectAll();
        }else{
            companies  = companyService.findCompanyByUserID(user.getId());
        }
        request.getSession().setAttribute("companies",companies);
        return "index";
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
}
