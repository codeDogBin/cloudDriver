package com.bin.controller;

import com.bin.domain.Fil;
import com.bin.domain.Folder;
import com.bin.service.FilService;

import com.bin.service.FolderService;
import com.bin.util.ImgUtil;
import com.bin.util.TimeUtil;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.*;


@Controller
public class FilController {
    @Autowired
    private FilService filService;
    @Autowired
    private FolderService folderService;
    @Autowired
    private AutoController autoController;

    private static String[] suffixes=new String[]{"jpg","JPG","png","PNG","jpeg","bmp"};
    public static boolean isImg(String suffix){
        for (String s : suffixes) {
            if(s.equals(suffix))
                return true;
        }
        return false;
    }

    /*
     * 功能描述 上传文件
     * @Author bin
     * @param multipartFiles
     * @param company_id
     * @param fway_id
     * @param request
     * @return java.lang.String
     */
    @ResponseBody
    @RequestMapping("/uploadFile.do")
    public Map uploadFile(@RequestParam("file")MultipartFile[]  multipartFiles,
                          String fway_id, HttpServletRequest request){
        Map<String,String> map = new HashMap();
        if (multipartFiles[0].isEmpty()) {
            map.put("state","FAIL");
            map.put("msg","文件为空,检查是否选择文件");
            return map;
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
                    ImgUtil.createImg(fil,request.getSession().getServletContext().getRealPath(""));
                    filService.insertFil(fil);
                } catch (Exception e) {
                    e.printStackTrace();
                    map.put("state","FAIL");
                    map.put("msg","文件上传失败，请检查重试");
                    return map;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            map.put("state","FAIL");
            map.put("msg","文件上传失败，请检查重试");
            return map;
        }
        map.put("state","OK");
        map.put("msg","文件上传成功");
        return map;
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
        String suffix = name.substring(name.lastIndexOf(".")+1);
        if(!isImg(suffix)){
            name = URLEncoder.encode(name, "UTF-8");
            //设置响应的contentType开启下载模式
            response.setContentType("application/x-msdownload");
            response.setHeader("Content-disposition","attachment;fileName="+name);
        }
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
    @ResponseBody
    @RequestMapping("/expireFile.do")
    public Map expireFil(int fil_id, int fway_id,HttpServletRequest request){
        Map<String,String> map = new HashMap<>();
        Fil fil = filService.findById(fil_id);
        try {
            fil.setState(false);
            fil = filService.chongMing(fil);
            if(isImg(fil.getName().substring(fil.getName().lastIndexOf(".")+1))){
                request.getSession().setAttribute("imgWay",fil.getImgWay());
                fil.setImgWay("");
            }
            fil.setDel_time(new Timestamp(System.currentTimeMillis()));
            filService.expireFil(fil);
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
    @ResponseBody
    @RequestMapping("/delImg.do")
    public String delImg(HttpServletRequest request){
        try {
            String imgway = (String)request.getSession().getAttribute("imgWay");
            if(imgway==null||"".equals(imgway)){
                return "OK";
            }
            File file=new File(request.getSession().getServletContext().getRealPath(""),imgway);
            Thread.sleep(300);
            file.delete();
            request.getSession().removeAttribute("imgWay");
        } catch (Exception e) {
            e.printStackTrace();
            return "FAIL";
        }
        return "OK";
    }


    /*
     * 功能描述 c重命名文件
     * @Author bin
     * @param fil_id
     * @param name
     * @param fway_id
     * @return java.util.Map
     */
    @ResponseBody
    @RequestMapping("renameFil.do")
    public Map renameFil(int fil_id,String name,int fway_id){
        Map<String,String> map = new HashMap<>();
        if(filService.findByNameFolid(name, fway_id)!=null){
            map.put("state","FAIL");
            map.put("msg","有重名文件夹");
            return map;
        }
        try {
            filService.renameFil(name,fil_id);
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
     * 功能描述 downZip
     * @Author bin
     * @param res
     * @return void
     */
    @RequestMapping("/downZip.do")
    public void downZip(HttpServletResponse res) throws IOException {
        String[] lastTime = TimeUtil.getLastMonth();
        String zipName = lastTime[0]+"新增客户文件.zip";
        String zipFilePath = "D:/couldriver"+File.separator+lastTime[1]+File.separator+lastTime[2]+File.separator+zipName;
        File file = new File(zipFilePath);
        if(!file.exists()){
            autoController.autoZip();
        }
        //IO流实现下载的功能
        res.setContentType("text/html; charset=UTF-8"); //设置编码字符
        res.setContentType("application/x-msdownload");//开启下载模式
        res.setHeader("Content-disposition", "attachment;filename="+new String(zipName.getBytes("utf-8"),"ISO-8859-1"));//设置下载的压缩文件名称
        OutputStream out = res.getOutputStream(); //创建页面返回方式为输出流，会自动弹出下载框
        //将打包后的文件写到客户端，输出的方法同上，使用缓冲流输出
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(zipFilePath));
        IOUtils.copy(bis, out);//拷贝
        bis.close();//关闭输入流
        out.flush();//释放缓存
        out.close();//关闭输出流
    }
}
