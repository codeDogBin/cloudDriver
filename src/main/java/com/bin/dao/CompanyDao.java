package com.bin.dao;

import com.bin.domain.Company;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface CompanyDao {
    @Insert("insert into company (name,way,ctime) values (#{name},#{way},#{ctime});")
    Integer insertCompany(Company company);
    @Select("select * from company where id = #{company_id}")
    Company findCompanyById(int companyId);
    @Select("select c.* from user_company as uc, company as c where uc.user_id=#{user.id} and uc.company_id = c.id")
    List<Company> findCompanyByUserID(int user_id);
    @Select("select * from company ")
    List<Company> selectAll();
}
