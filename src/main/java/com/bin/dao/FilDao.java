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

    @Select("select * from fil where id = #{fil_id} and state = 1")
    Fil findById(int fil_id);

    @Select("select * from fil where state = 0")
    List<Fil> allExpire();

    @Update("update fil set state = 0 where id = #{fil_id}")
    void expireFileById(int fil_id );

    @Delete("delete from fil where id = #{id}")
    void delFilById(int id);


}
