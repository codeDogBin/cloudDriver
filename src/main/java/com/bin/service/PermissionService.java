package com.bin.service;

import com.bin.dao.PermissionDao;
import com.bin.domain.Permission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("permissionService")
public class PermissionService {
    @Autowired
    private PermissionDao permissionDao;
    /*
     * 功能描述 查找权限信息
     * @Author bin
     * @param permission 
     * @return com.bin.domain.Permission        
     */
    public Permission findPermission(int permission){
        return permissionDao.findPermission(permission);
    }
}
