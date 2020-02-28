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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

//    @RequestMapping("/toLogin")
//    public String toLogin(){
//        return "login";
//    }

    /*
     * 功能描述 登录
     * @Author bin
     * @param name
     * @param password
     * @param request 
     * @return java.lang.String        
     */
    @ResponseBody
    @RequestMapping("/login.do")
    public String login(String username, String password, HttpServletRequest request){
        User user = userService.login(username, password);
        if(user == null){
            return "FAIL";
        }
        Permission permission = permissionService.findPermission(user.getPermission_id());
        request.getSession().setAttribute("permission",permission);
        request.getSession().setAttribute("user",user);
        return "OK";
    }
    /*
     * 功能描述 前往主页 并且将公司信息存入requester
     * @Author bin
     * @param request 
     * @return java.lang.String        
     */
    @ResponseBody
    @RequestMapping("/toIndexAjax.do")
    public Map index(HttpServletRequest request){
        User user = (User)request.getSession().getAttribute("user");
        Permission permission = (Permission)request.getSession().getAttribute("permission");
        List<Company> companies;
        if(permission.isStaff()){//如果当前权限是读取所有公司的权限
            companies = companyService.selectAll();
        }else{
            companies  = companyService.findCompanyByUserIDLimit(user.getId(),0);
        }
        Map<String,Object> map= new HashMap();
        map.put("userName",user.getName());
        map.put("permission",permission);
        map.put("companies",companies);
        return map;
    }

    @RequestMapping(value = "/toIndex.do",produces = "text/html;charset=UTF-8")
    public String toIndex(){
        return "main";
    }

    @RequestMapping(value = "/toRegister.do",produces = "text/html;charset=UTF-8")
    public String toRegister(){
        return "register";
    }

    @RequestMapping(value = "/dispatcher.do",produces = "text/html;charset=UTF-8")
    public String toDiapatcherCrop(){
        return "dispatcherCrop";
    }

    @RequestMapping(value = "/restore.do",produces = "text/html;charset=UTF-8")
    public String toRestore(){
        return "folder";
    }



    /*
     * 功能描述 查询的接口
     * @Author bin
     * @param name
     * @return java.util.Map
     */
    @ResponseBody
    @RequestMapping("/selectCompanyByNameIndexAJAX.do")
    public Map selectCompanyByNameIndexAJAX(String name){
        List<Company> companies;
        companies = companyService.SelectByName(name);
        Map<String, Object> Map = new HashMap<>();
        Map.put("companies",companies);
        return Map;
    }
//    /*
//     * 功能描述 主页的翻页工具
//     * @Author bin
//     * @param start
//     * @param name
//     * @param request
//     * @return java.util.List
//     */
//    @ResponseBody
//    @RequestMapping("/indexPageAJAX.do")
//    public List indexAJAX(int start,String name,HttpServletRequest request){
//        User user = (User)request.getSession().getAttribute("user");
//        Permission permission = (Permission)request.getSession().getAttribute("permission");
//        List<Company> companies;
//        if(permission.isStaff()){//如果当前权限是读取所有公司的权限
//            if(name==null||"".equals(name)){
//                companies = companyService.selectAll(start,20);
//            }else {
//                companies= companyService.SelectByName(name,start);
//            }
//        }else{
//            companies  = companyService.findCompanyByUserIDLimit(user.getId(),start);
//        }
//        return companies;
//    }


    /*
     * 功能描述 注册
     * @Author bin
     * @param name
    * @param password
    * @param permission_id
    * @param request
     * @return java.lang.String
     */
    @ResponseBody
    @RequestMapping("/register.do")
    public String register(String username,
                           String password,
                           String registrationCode){
        if(userService.findByName(username)!=null){
            return "FAIL";
        }
        User user = new User();
        user.setName(username);
        user.setPassword(password);
        if (registrationCode.equals("myyg")){
            user.setPermission_id(2);

        }else if (registrationCode.equals("mycw")){
            user.setPermission_id(3);
        }else if(registrationCode.equals("myadmin")){
            user.setPermission_id(1);
        }else {
            user.setPermission_id(4);
        }
        boolean register = userService.register(user);
        if(register){
            return "OK";
        }
        return "FAIL";
    }
    /*
     * 功能描述 注册检查用户名是否已存在
     * @Author bin
     * @param userName
     * @return java.util.List
     */
    @ResponseBody
    @RequestMapping("/examineUsernameAJAX.do")
    public String examineUsernameAJAX(String username){
        User user = userService.findByName(username);
        if (user==null){
            return "OK";
        }
        return "FAIL";
    }
    @ResponseBody
    @RequestMapping("/examineRegistrationCodeAJAX.do")
    public Map examineRegistrationCodeAJAX(String registrationCode){
        Map<String,String> map= new HashMap();
        if (registrationCode.equals("myyg")){
            map.put("state","OK");
            map.put("msg","注册为员工");
            return map;
        }else if (registrationCode.equals("mycw")){
            map.put("state","OK");
            map.put("msg","注册为财务");
            return map;
        }else if(registrationCode.equals("myadmin")){
            map.put("state","OK");
            map.put("msg","注册为管理员");
            return map;
        }
        map.put("state","FAIL");
        return map;
    }

    /*
     * 功能描述 模糊查找 用户名
     * @Author bin
     * @param username
     * @return java.util.List
     */
    @ResponseBody
    @RequestMapping("/findUserByNameLikeAJAX.do")
    public List findUserByNameLikeAJAX(String username){
        return userService.findByNameLike(username);
    }

}
