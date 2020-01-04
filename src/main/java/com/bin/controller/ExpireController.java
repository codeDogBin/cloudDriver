package com.bin.controller;

import com.bin.domain.Fil;
import com.bin.domain.Folder;
import com.bin.service.FilService;
import com.bin.service.FolderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class ExpireController {
    @Autowired
    private FolderService folderService;
    @Autowired
    private FilService filService;
    /*
     * 功能描述 去过期文件页
     * @Author bin
     * @param request 
     * @return java.lang.String        
     */
    @RequestMapping("toAllExpire")
    public String allExpire(HttpServletRequest request){
        List<Fil> fils = filService.allExpire();
        List<Folder> folders = folderService.allExpire();
        request.setAttribute("fils",fils);
        request.setAttribute("folders",folders);
        return "expire";
    }
}
