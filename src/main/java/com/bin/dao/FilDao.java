package com.bin.dao;

import com.bin.domain.Fil;
import org.apache.ibatis.annotations.*;
import java.sql.Timestamp;
import java.util.List;

public interface FilDao {
    @Insert("insert into fil (name,way,imgway,fol_id,ctime,state) values(#{name},#{way},#{imgWay},#{fol_id},#{ctime},1)")
    Integer insert(Fil fil);

    @Select("select * from fil where fol_id = #{fol_id} and state = 1")
    List<Fil> findByFid(int fol_id);

    @Select("select * from fil where id = #{fil_id} and state = 1 limit 1")
    Fil findById(int fil_id);

    @Select("select * from fil where id = #{fil_id} and state = 0 limit 1")
    Fil findExpireById(int fil_id);

    @Select("select * from fil where name =#{name} and fol_id =#{fol_id} and state = 0")
    Fil findExpireByNameFolid(@Param("name") String name,@Param("fol_id") int fol_id);

    @Select("select * from fil where state = 0 order by del_time desc limit 1000")
    List<Fil> allExpire();

    @Update("update fil set state = 0 ,name= #{name}, imgway=#{imgWay},del_time= #{del_time} where id = #{id}")
    void expireFileById(Fil fil);

    @Delete("delete from fil where id = #{fil_id}")
    void delFilById(int fil_id);

    @Select("select * from fil where name= #{name} and fol_id = #{fol_id} and state = #{state} limit 1")
    Fil findByNameFidState(Fil fil);

    @Select("select * from fil where state = 0 and del_time < #{timestamp}")
    List<Fil> getExpireFil(Timestamp timestamp);

    @Update("update fil set state = 1 ,name= #{name} where id = #{id}")
    void recoverFil(Fil fil);

    @Select("select * from fil where state = 1 and ctime between #{startTime} and #{endTime} ")
    List<Fil> getFilByMonth(@Param("startTime") Timestamp startTime,@Param("endTime") Timestamp endTime);

    @Select("select * from fil where name= #{name} and fol_id = #{fol_id} and state = 1 limit 1")
    Fil findByNameFolid(@Param("name") String name,@Param("fol_id") int fol_id);

    @Update("update fil set name= #{name} where id = #{id}")
    void renameFil(@Param("name") String name, @Param("id") int fil_id);

    @Update("update fil set imgway= #{imgWay} where id = #{id}")
    void updateImgWay(Fil fil);
}
