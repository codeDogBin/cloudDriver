package com.bin.controller;

import com.bin.domain.Company;
import com.bin.domain.Fil;
import com.bin.domain.Folder;
import com.bin.service.CompanyService;
import com.bin.service.FilService;
import com.bin.service.FolderService;
import com.bin.util.CreateFolderUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

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
    public String toFolder(int company_id,
                           int fway_id,
                           HttpServletRequest request){
        List<Folder> folders = folderService.findByFidCid(fway_id, company_id);
        List<Fil> fils = filService.FindByFid(fway_id);
        Company company = companyService.selectCompany(company_id);
        List<Folder> ways = new ArrayList<>();
        findWay(fway_id,ways);
        request.setAttribute("company",company);
        request.setAttribute("ways",ways);
        request.setAttribute("company_id",company_id);
        request.setAttribute("fway_id",fway_id);
        if(folders.size() == 0 && fils.size() ==0){
            request.setAttribute("msg","当前目录无内容");
            return "folder";
        }
        request.setAttribute("folders",folders);
        request.setAttribute("fils",fils);
            return "folder";
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
     public String  createFolder(String name,int fway_id,int company_id){
         Folder byNameFidCid = folderService.findByNameFidCid(name, fway_id, company_id);
         if(byNameFidCid != null){
             return "有同名文件夹";
         }
         String fway =fway_id == 0 ?companyService.selectCompany(company_id).getWay():folderService.findByFidAsId(fway_id).getWay();
         try {
             fway = CreateFolderUtil.createFloder(fway);
             Folder folder = new Folder();
             folder.setName(name);
             folder.setFway_id(fway_id);
             folder.setCompany_id(company_id);
             folder.setWay(fway);
             folder.setCtime(new Timestamp(System.currentTimeMillis()));
             folderService.insertFolder(folder);
             return "创建成功";
         } catch (Exception e) {
             e.printStackTrace();
         }
         return "创建失败";
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
    @RequestMapping("/expireFolder.do")
     public String expireFolder(int folder_id,
                                int fway_id,
                                int company_id,
                                HttpServletRequest request){
         Folder folder = folderService.findByFidAsId(folder_id);
         folder.setState(false);
         folder = folderService.chongMing(folder);
         folderService.expireFol(folder);
         request.setAttribute("msg","删除成功");
         return "forward:toFolder.do?company_id="+company_id+"&fway_id="+fway_id;
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
//    /*
//     * 功能描述 ergodicFolFil 遍历文件夹下所有的子文件夹和文件
//     * @Author bin
//     * @param fol_id
//     * @param company_id
//     * @param list
//     * @return void
//     */
//    public void ergodicFolFilByFolId(int fol_id,int company_id ,List list){
//        List<Folder> folderList = folderService.findByFidCid(fol_id, company_id);
//        List<Fil> filList = filService.FindByFid(fol_id);
//        if(folderList.size()!=0){
//            for (Folder folder : folderList) {
//                ergodicFolFilByFolId(folder.getId(),company_id,list);
//                list.add(folder);
//            }
//        }
//        if(filList.size() !=0){
//            for (Fil fil : filList) {
//                list.add(fil);
//            }
//        }
//    }


}
