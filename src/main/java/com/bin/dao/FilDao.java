package com.bin.dao;

import com.bin.domain.Fil;
import lombok.Setter;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface FilDao {
    @Insert("insert into fil (name,way,fol_id,ctime,state) values(#{name},#{way},#{fol_id},#{ctime},1)")
    Integer insert(Fil fil);

    @Select("select * from fil where fol_id = #{fol_id} and state = 1")
    List<Fil> findByFid(int fol_id);

    @Select("select * from fil where id = #{fil_id} and state = 1 limit 1")
    Fil findById(int fil_id);

    @Select("select * from fil where id = #{fil_id} and state = 0 limit 1")
    Fil findExpireById(int fil_id);

    @Select("select * from fil where state = 0")
    List<Fil> allExpire();

    @Update("update fil set state = 0 ,name= #{name} where id = #{id}")
    void expireFileById(Fil fil);

    @Delete("delete from fil where id = #{fil_id}")
    void delFilById(int fil_id);

    @Select("select * from fil where name= #{name} and fol_id = #{fol_id} and state = #{state} limit 1")
    Fil findByNameFidState(Fil fil);

    @Update("update fil set state = 1 ,name= #{name} where id = #{id}")
    void recoverFil(Fil fil);
}
