package com.bin.dao;

import com.bin.domain.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface UserDao {

    @Insert("insert into user(name,password,permission_id) values (#{name},#{password},#{permission_id});")
    Integer insertUser(User user);

    @Select("select * from user where name = #{name} and password = #{password} limit 1 ")
    User findUser(@Param("name")String name,@Param("password") String password);

    @Update("update user set last_login=#{last_login}, count = #{count} where name = #{name}")
    void updateUserLoginTime(User user);

    @Select("select * from user")
    List<User> findAll();

    @Select("select * from user where permission_id = 4")
    List<User> findAllCus();

    @Select("select * from user where name like #{userName} and permission_id =4 limit 0, 20")
    List<User> findByName(String userName);
}
