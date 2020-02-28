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

    @Select("select * from company order by ctime desc limit 1000 ")
    List<Company> selectAll();

    @Select("select * from company where name like #{name} limit 100")
    List<Company> selectByName(@Param("name") String name);

    @Select("select count(id) from company")
    Integer getAllCompanyCount();

    @Select("select count(id) from company where name like #{name}")
    Integer selectCountByName(String companyName);

    @Select("select * from company where name like #{name}")
    List<Company> selectByNameNoPage(String companyName);

    @Select("select * from company where name = #{name} limit 1")
    Company findCompanyByName(String name);
}
