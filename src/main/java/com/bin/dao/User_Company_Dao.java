package com.bin.dao;


import com.bin.domain.User_Company;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface User_Company_Dao {
    @Insert("insert into user_company (user_id,company_id,ctime) values(#{user_id},#{company_id},#{ctime})   ")
    Integer insert(User_Company user_company);

    @Select("select * from user_company where user_id = #{user_id}")
    List<User_Company> selectCompanyByUserId(int userId);

    @Delete("delete from user_company where user_id = #{user_id} and company_id = #{company_id}")
    void delete(User_Company user_company);
}
