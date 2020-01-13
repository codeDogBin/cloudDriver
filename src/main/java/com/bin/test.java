package com.bin;


import com.bin.controller.DownZipController;
import com.bin.service.CompanyService;
import com.bin.service.FolderService;
import com.fasterxml.jackson.databind.deser.std.DateDeserializers;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import java.io.File;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;


public class test {
    private static ApplicationContext apc;
    static{ apc = new ClassPathXmlApplicationContext("applicationContext.xml");}
    public static void main(String[] args) {
        CompanyService companyService = apc.getBean("companyService",CompanyService.class);
        FolderService folderService =apc.getBean("folderService",FolderService.class);

//        Company company = new Company();
//        company.setName("彬彬");
//        company.setWay("123123");
//        company.setCtime(new Timestamp(System.currentTimeMillis()));
//        cs.registerCompany(company);
//        System.out.println(company.getId());
//        Timestamp timestamp1 = new Timestamp(System.currentTimeMillis()-24*3600*1000);
//        Timestamp timestamp2 = new Timestamp(System.currentTimeMillis());
//        System.out.println(timestamp1);
//        System.out.println(timestamp2);
//        List<Folder> folders = folderService.getExpireFol(timestamp1);
//        List<Folder> folders1 = folderService.getExpireFol(timestamp2);
//        System.out.println(folders);
//        System.out.println(folders1);
//        Test test = new Test();
//        test.deleteFolder(new File("C:\\Users\\lb\\Desktop\\1"));

        HashMap<Integer, String> Map = new HashMap<>();
        folderService.allContent(Map);
    }
}
