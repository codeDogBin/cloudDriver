package com.bin.dao;

import com.bin.domain.Company;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface CompanyDao {
    @Insert("insert into company (name,way,ctime) values (#{name},#{way},#{ctime});")
    @Options(useGeneratedKeys = true , keyProperty = "id", keyColumn = "id")
    Integer insertCompany(Company company);

    @Select("select * from company where id = #{company_id} limit 1")
    Company findCompanyById(int companyId);

    @Select("select c.* from user_company as uc, company as c where uc.user_id=#{user_id} and uc.company_id = c.id limit #{start},20")
    List<Company> findCompanyByUserIDLimit(@Param("user_id") int user_id, @Param("start") int start);

    @Select("select c.* from user_company as uc, company as c where uc.user_id=#{user_id} and uc.company_id = c.id ")
    List<Company> findCompanyByUserID(@Param("user_id") int user_id);

    @Select("select * from company limit  #{start}, #{pageSize} ")
    List<Company> selectAll(@Param("start") int start,@Param("pageSize") int pageSize);

    @Select("select * from company where name like #{name} limit #{start},20")
    List<Company> selectByName(@Param("name") String name,@Param("start") int start);

    @Select("select count(id) from company")
    Integer getAllCompanyCount();

    @Select("select count(id) from company where name like #{name}")
    Integer selectCountByName(String companyName);

    @Select("select * from company where name like #{name}")
    List<Company> selectByNameNoPage(String companyName);
}
