package com.bin.controller;

import com.bin.domain.Fil;
import com.bin.domain.Folder;
import com.bin.service.FilService;

import com.bin.service.FolderService;
import com.bin.util.TimeUtil;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;


@Controller
public class FilController {
    @Autowired
    private FilService filService;
    @Autowired
    private FolderService folderService;
    @Autowired
    private AutoController autoController;
    /*
     * 功能描述 上传文件
     * @Author bin
     * @param multipartFiles
     * @param company_id
     * @param fway_id
     * @param request
     * @return java.lang.String
     */
    @RequestMapping("/uploadFile.do")
    public String uploadFile(List<MultipartFile>  multipartFiles,
                             String company_id,
                             String fway_id,
                             HttpServletRequest request){
        if (multipartFiles.get(0).isEmpty()) {
            request.setAttribute("msg",  "没有选择上传文件");
            return "forward:toFolder.do?company_id="+company_id+"&fway_id="+fway_id;
        }
        Folder folder = folderService.findByFidAsId(Integer.parseInt(fway_id));
        String filePath = folder.getWay();
        try {
            for (MultipartFile origFile : multipartFiles) {
                String contentType = origFile.getContentType();
                String fileName = origFile.getOriginalFilename();
                byte[] bytes = origFile.getBytes();
                String way =UUID.randomUUID().toString().replaceAll("-","");
                System.out.println("上传原文件名为-->" + fileName);
                System.out.println("上传文件名为-->" + way);
                System.out.println("上传文件类型为-->" + contentType);
                System.out.println("上传文件大小为-->"+bytes.length);
                System.out.println("上传目的地为-->"+filePath);
                Fil fil = new Fil();
                fil.setName(fileName);
                fil.setWay(folder.getWay()+File.separator+way);
                fil.setFol_id(Integer.parseInt(fway_id));
                fil.setCtime(new Timestamp(System.currentTimeMillis()));
                fil.setState(true);
                fil = filService.chongMing(fil);
                try {
                    //上传目的地（staticResourcesTest文件夹下）
                    File file = new File(filePath, way);
                    FileUtils.writeByteArrayToFile(file,origFile.getBytes());
                    filService.insertFil(fil);
                    request.setAttribute("msg",  "上传成功");
                } catch (Exception e) {
                    request.setAttribute("msg",  "上传出错");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "forward:toFolder.do?company_id="+company_id+"&fway_id="+fway_id;
        }
        return "forward:toFolder.do?company_id="+company_id+"&fway_id="+fway_id;
    }
    /*
     * 功能描述 获取文件
     * @Author bin
     * @param way
     * @param name
     * @param response
     * @return void
     */
    @RequestMapping("/getFile.do")
    public void getFile(int id, String name, HttpServletResponse response) throws Exception { ;
        Fil fil = filService.findById(id);
        File file = new File(fil.getWay());
        name = URLEncoder.encode(name, "UTF-8");
        //设置响应的contentType开启下载模式
        response.setContentType("application/x-msdownload");
        response.setHeader("Content-disposition","attachment;fileName="+name);
        InputStream inputStream = new FileInputStream(file);//获取文件的流
        OutputStream outputStream = response.getOutputStream();//获取输出流
        IOUtils.copy(inputStream, outputStream);//
        inputStream.close();
        outputStream.close();
    }
    /*
     * 功能描述 文件设置为过期文件
     * @Author bin
     * @param fil_id
     * @param company_id
     * @param fway_id
     * @param request
     * @return java.lang.String
     */
    @RequestMapping("/expireFile.do")
    public String expireFil(int fil_id,
                            String company_id,
                            String fway_id,
                            String name,
                            HttpServletRequest request){
        Fil fil = filService.findById(fil_id);
        try {
            Fil expirefil = filService.findExpireByNameFolid(name,Integer.parseInt(fway_id));
            if(expirefil != null){
                fil = filService.chongMing(fil);
            }
            fil.setState(false);
            fil.setDel_time(new Timestamp(System.currentTimeMillis()));
            filService.expireFil(fil);
            request.setAttribute("msg","删除成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "forward:toFolder.do?company_id="+company_id+"&fway_id="+fway_id;
    }
    @RequestMapping("/downZip.do")
    public void downZip(HttpServletResponse res) throws IOException {
        String[] lastTime = TimeUtil.getLastMonth();
        String zipName = lastTime[0]+"新增客户文件.zip";
        String zipFilePath = "D:/couldriver"+File.separator+lastTime[1]+File.separator+lastTime[2]+File.separator+zipName;
        System.out.println(zipFilePath);
        File file = new File(zipFilePath);
        if(!file.exists()){
            autoController.autoZip();
        }
        //IO流实现下载的功能
        res.setContentType("text/html; charset=UTF-8"); //设置编码字符
        res.setContentType("application/octet-stream"); //设置内容类型为下载类型
        OutputStream out = res.getOutputStream(); //创建页面返回方式为输出流，会自动弹出下载框
        res.setHeader("Content-disposition", "attachment;filename="+new String(zipName.getBytes("utf-8"),"ISO-8859-1"));//设置下载的压缩文件名称
        //将打包后的文件写到客户端，输出的方法同上，使用缓冲流输出
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(zipFilePath));
        IOUtils.copy(bis, out);//拷贝
        bis.close();//关闭输入流
        out.flush();//释放缓存
        out.close();//关闭输出流
    }
}
