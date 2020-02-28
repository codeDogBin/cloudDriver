package com.bin.service;

import com.bin.dao.UserDao;
import com.bin.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.sql.Timestamp;
import java.util.List;

@Service("userService")
public class UserService {
    @Autowired
    private UserDao dao;
    /*
     * 功能描述 注册
     * @Author bin
     * @param user 
     * @return boolean        
     */
    public boolean register(User user){
        Integer x = null;
        try {
            x = dao.insertUser(user);
            return x==1?true:false;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    /*
     * 功能描述 登录
     * @Author bin
     * @param name
 * @param password 
     * @return com.bin.domain.User        
     */
    public  User login(String name,String password){
        User user = dao.findUser(name, password);
        if(user == null){
            return null;
        }
        user.setLast_login(new Timestamp(System.currentTimeMillis()));
         if(user.getCount() == null){
             user.setCount(1);
         }user.setCount(user.getCount()+1);
        dao.updateUserLoginTime(user);
        return user;
    }
    /*
     * 功能描述 查找所有用户
     * @Author bin
     * @param  
     * @return java.util.List<com.bin.domain.User>        
     */
    public List<User> findAll() {
        return dao.findAll();
    }
    /*
     * 功能描述 查找所有客户
     * @Author bin
     * @param  
     * @return java.util.List<com.bin.domain.User>        
     */
    public List<User> findAllCus(){
       return dao.findAllCus();
    }
    /*
     * 功能描述
     * @Author bin
     * @param null
     * @return
     */
    public List<User> findByNameLike(String username) {
        username = "%"+username+"%";
        return dao.findByNameLike(username);
    }

    /*
     * 功能描述 findByName
     * @Author bin
     * @param username 
     * @return com.bin.domain.User        
     */
    public User findByName(String username) {
        return dao.findByName(username);
    }
}
