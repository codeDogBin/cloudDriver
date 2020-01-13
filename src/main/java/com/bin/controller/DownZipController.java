package com.bin.controller;

import com.bin.domain.Company;
import com.bin.domain.Fil;
import com.bin.domain.Folder;
import com.bin.service.CompanyService;
import com.bin.service.FilService;
import com.bin.service.FolderService;
import com.bin.util.ZipUtil;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.zip.ZipOutputStream;


@Controller
public class DownZipController {
    @Autowired
    private FilService filService;
    @Autowired
    private FolderService folderService;
    @Autowired
    private CompanyService companyService;
    @RequestMapping("/downZip")
    public void downZip(HttpServletResponse res) throws IOException {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis()-30*24*3600*1000l);
        //找到最近一个月内的所有文件
        List<Fil> fils = filService.getFilByMonth(timestamp);
        Map<Integer,String> wayMap=new HashMap();
        folderService.allContent(wayMap);
        //将所有文件的名字和文件存好
        List<String[]> filNameAndWay = filNameAndWay(fils,wayMap);
        //设置日期格式
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String format = simpleDateFormat.format(new Date());
        //设置存储地址
        String zipBasePath = "D:/couldriver";
        String zipName = "temp.zip";
        String zipFilePath = zipBasePath + File.separator + zipName;
        //压缩文件的-创建
        File zip = new File(zipFilePath);
        if (!zip.exists()) {
            zip.createNewFile();
        }
        try {
                ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zip));
                ZipUtil.zipFile(filNameAndWay, zos); //调用util工具创建
                zos.close();
        } catch (IOException e) {
                e.printStackTrace();
        }
        res.setContentType("text/html; charset=UTF-8"); //设置编码字符
        res.setContentType("application/octet-stream"); //设置内容类型为下载类型
        OutputStream out = res.getOutputStream(); //创建页面返回方式为输出流，会自动弹出下载框
        res.setHeader("Content-disposition", "attachment;filename="+format+zipName);//设置下载的压缩文件名称
        //将打包后的文件写到客户端，输出的方法同上，使用缓冲流输出
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(zipFilePath));
        IOUtils.copy(bis, out);//
        bis.close();
        out.flush();//释放缓存
        out.close();//关闭输出流
    }
        /*
         * 功能描述 filNameAndWay 获取文件的全路径名和名称
         * @Author bin
         * @param fils 文件集合
         * @param wayMap 路径集合
         * @return java.util.List<java.lang.String[]>
         */
    private static List<String[]> filNameAndWay(List<Fil> fils,Map<Integer,String> wayMap){
        List <String[]> filNameAndWay = new ArrayList<>();
        for (Fil fil : fils) {
            //如果父文件夹为null，则跳过
            if(wayMap.get(fil.getFol_id())==null)
                continue;
            filNameAndWay.add(new String[]{wayMap.get(fil.getFol_id())+fil.getName(),fil.getWay()});
        }
        return filNameAndWay;
    }
}




