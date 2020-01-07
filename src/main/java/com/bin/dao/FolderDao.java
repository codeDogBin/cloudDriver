package com.bin.dao;

import com.bin.domain.Folder;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface FolderDao {
    @Insert("insert into folder (name,way,fway_id,ctime,company_id,state) values(#{name},#{way},#{fway_id},#{ctime},#{company_id},1)   ")
    Integer insert(Folder folder);

    @Select("select * from folder where fway_id = #{fway_id} and company_id = #{company_id} and state = 1")
    List<Folder> findByFidCid(@Param("fway_id") int fway_id,@Param("company_id") int company_id);

    @Select("select * from folder where name= #{name} and fway_id = #{fway_id} and company_id = #{company_id} and state = 1 limit 1")
    Folder findByNameFidCid(@Param("name") String name,@Param("fway_id") int fway_id,@Param("company_id") int company_id);

    @Select("select * from folder where id = #{fway_id} and state = 1 limit 1")
    Folder findByFidAsId(int fway_id);

    @Select("select * from folder where id = #{id} and state = 0 limit 1")
    Folder findExpireByID(int id);

    @Delete("delete from folder where id = #{fol_id}")
    void delFolById(int fol_id);

    @Select("select * from folder where state = 0")
    List<Folder> allExpire();

    @Select("select * from folder where name=#{name} and fway_id = #{fway_id} and company_id = #{company_id} and state = #{state} limit 1")
    Folder findByNameFidCidState(Folder folder);

    @Update("update folder set state = 0, name = #{name}  where id = #{id}")
    void expireFol(Folder folder);

    @Update("update folder set state = 1, name = #{name}  where id = #{id}")
    void recoverFol(Folder fol);
}
