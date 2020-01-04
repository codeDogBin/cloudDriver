package com.bin.dao;


import com.bin.domain.Permission;
import org.apache.ibatis.annotations.Select;

public interface PermissionDao {
    @Select("select * from Permission where id= #{permission_id}")
    Permission findPermission(int permission_id);
}
