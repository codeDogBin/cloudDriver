package com.bin.controller;

import com.bin.domain.Fil;
import com.bin.domain.Folder;
import com.bin.service.FilService;
import com.bin.service.FolderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    @RequestMapping("/toAllExpire")
    public String allExpire(HttpServletRequest request){
        return "recycleBin";
    }

    @ResponseBody
    @RequestMapping("/getAllExpire.do")
    public Map getAllExpire () {
        List<Fil> fils = filService.allExpire();
        List<Folder> folders = folderService.allExpire();
        Map<String,List> map = new HashMap<>();
        map.put("files",fils);
        map.put("folders",folders);
        return map;
    }

    @ResponseBody
    @RequestMapping("/recoverFil.do")
    public String recoverFil(int fil_id){
        Fil fil = filService.findExpireById(fil_id);
        fil.setState(true);
        fil = filService.chongMing(fil);
        filService.recoverFil(fil);
        return "OK";
    }

    @ResponseBody
    @RequestMapping("/recoverFolder.do")
    public String recoverFolder(int fol_id){
        Folder fol = folderService.findExpireByID(fol_id);
        fol.setState(true);
        fol=folderService.chongMing(fol);
        folderService.recoverFol(fol);
        return "OK";
    }

}
