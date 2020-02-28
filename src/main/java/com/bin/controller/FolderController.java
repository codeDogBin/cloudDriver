package com.bin.controller;

import com.bin.domain.*;
import com.bin.service.CompanyService;
import com.bin.service.FilService;
import com.bin.service.FolderService;
import com.bin.util.CreateFolderUtil;
import com.bin.util.ImgUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class FolderController {
     @Autowired
     private FolderService folderService;
     @Autowired
     private CompanyService companyService;
     @Autowired
     private FilService filService;


     /*
      * 功能描述 去文件夹页面
      * @Author bin
      * @param company_id
      * @param fway_id
      * @param request
      * @return java.lang.String
      */
    @RequestMapping("/toFolder.do")
    public String toFolder(){
        return "folder";
    }
    @ResponseBody
    @RequestMapping("/toFolderAJAX.do")
    public String toFolderAjax(int company_id,
                           int tofway_id,
                           HttpServletRequest request){
        try {
            HttpSession session = request.getSession();
            session.setAttribute("company_id",company_id);
            session.setAttribute("fway_id",tofway_id);
            System.out.println("设置session：company_id:"+company_id+"fway_id:"+tofway_id);
        } catch (Exception e) {
            e.printStackTrace();
            return "FAIL";
        }
        return "OK";
    }

    @ResponseBody
    @RequestMapping("/getFolderAJAX.do")
    public Map getFolder (HttpServletRequest req) throws IOException {
        Map<String,Object> map = new HashMap<>(50);
        int fway_id = (int)req.getSession().getAttribute("fway_id");
        int company_id = (int)req.getSession().getAttribute("company_id");
        System.out.println("查询到session：company_id:"+company_id+"fway_id:"+fway_id);
        //查询当前目录的文件夹
        List<Folder> folders = folderService.findByFidCid(fway_id, company_id);
        //查问当前目录的文件
        Company company = companyService.selectCompany(company_id);
        List<Fil> fils = filService.FindByFid(fway_id);
        String way =req.getSession().getServletContext().getRealPath("");
        //判断文件是否有文件图片信息
        for (Fil fil : fils) {
            if(fil.getImgWay()==null||"".equals(fil.getImgWay())){
                ImgUtil.createImg(fil,way);
                filService.updateImgWay(fil);
            }
        }
        List<Folder> ways = new ArrayList<>();
        findWay(fway_id,ways);
        Permission permission =(Permission)req.getSession().getAttribute("permission");
        User user =(User)req.getSession().getAttribute("user");
        map.put("user",user);
        map.put("permission",permission);
        map.put("ways",ways);
        map.put("company",company);
        map.put("fway_id",fway_id);
        map.put("folders",folders);
        map.put("fils",fils);
        return map;
    }

    /*
     * 功能描述 创建文件夹
     * @Author bin
     * @param name
     * @param fway_id
     * @param company_id
     * @return java.lang.String
     */
     @ResponseBody
     @RequestMapping("/createFolder.do")
     public  Map  createFolder(String name,int fway_id,int company_id){
         Map<String,String> map = new HashMap<>();
         //查询当前文件夹下是否有同名文件夹
         Folder byNameFidCid = folderService.findByNameFidCid(name, fway_id, company_id);
         if(byNameFidCid != null){
             map.put("state","FAIL");
             map.put("msg","有同名文件夹");
             return map;
         }
         String fway =fway_id == 0 ?companyService.selectCompany(company_id).getWay():folderService.findByFidAsId(fway_id).getWay();
         try {
             fway = CreateFolderUtil.createFolder(fway);
             Folder folder = new Folder();
             folder.setName(name);
             folder.setFway_id(fway_id);
             folder.setCompany_id(company_id);
             folder.setWay(fway);
             folder.setCtime(new Timestamp(System.currentTimeMillis()));
             folderService.insertFolder(folder);
             map.put("state","OK");
             map.put("msg","创建成功");
             return map;
         } catch (Exception e) {
             e.printStackTrace();
         }
         map.put("state","FAIL");
         map.put("msg","创建失败，请检查后重试");
         return map;
     }


     /*
      * 功能描述 重命名文件夹
      * @Author bin
      * @param name
      * @param folder
      * @return java.util.Map
      */
     @ResponseBody
     @RequestMapping("/renameFolder.do")
     public Map renameFolder(String name,int folder_id,int fway_id){
         Map<String,String> map = new HashMap<>();
         if(folderService.findByNameFidCid(name, fway_id, fway_id)!=null){
             map.put("state","FAIL");
             map.put("msg","有重名文件夹");
             return map;
         }
         try {
             folderService.renameFol(name,folder_id);
         } catch (Exception e) {
             e.printStackTrace();
             map.put("state","FAIL");
             map.put("msg","请重试");
             return map;
         }
         map.put("state","OK");
         map.put("msg","重命名成功");
         return map;
     }



     /*
     * 功能描述 删除文件夹
     * @Author bin
     * @param folder_id
     * @param fway_id
     * @param company_id
     * @param request
     * @return java.lang.String
     */
    @ResponseBody
    @RequestMapping("/expireFolderAJAX.do")
     public Map expireFolderAJAX(int folder_id){
        Map<String,String> map = new HashMap<>();
        try {
            Folder folder = folderService.findByFidAsId(folder_id);
            folder.setState(false);
            folder.setDel_time(new Timestamp(System.currentTimeMillis()));
            folder = folderService.chongMing(folder);
            folderService.expireFol(folder);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("state","FAIL");
            map.put("msg","删除失败，检查后重试");
            return map;
        }
        map.put("state","OK");
        map.put("msg","删除成功");
        return map;
     }
    /*
     * 功能描述 findWay 查找当前文件夹所在路径
     * @Author bin
     * @param folder_id
     * @param way
     * @return void
     */
    public  void findWay(int folder_id, List <Folder> way){
        if(folder_id == 0){
            return ;
        }
        Folder folder = folderService.findByFidAsId(folder_id);
        findWay(folder.getFway_id(),way);
        way.add(folder);
    }


}
