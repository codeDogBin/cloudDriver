package com.bin;


import com.bin.service.CompanyService;
import com.bin.service.FilService;
import com.bin.service.FolderService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;


public class test {
    private static ApplicationContext apc;
    static{ apc = new ClassPathXmlApplicationContext("applicationContext.xml");}
    public static void main(String[] args) throws IOException {
        CompanyService companyService = apc.getBean("companyService",CompanyService.class);
        FolderService folderService =apc.getBean("folderService",FolderService.class);
        FilService filService =apc.getBean("filService", FilService.class);

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

//        HashMap<Integer, String> Map = new HashMap<>();
//        folderService.allContent(Map);
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
//        Date date = new Date();
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(date);
//        calendar.add(Calendar.MONTH,calendar.get(Calendar.MONTH)-1);
//        date = calendar.getTime();String yearMonth = format.format(date);
//        System.out.println(yearMonth);
//        String year=yearMonth.split("-")[0];
//        System.out.println(year);
//        String month=yearMonth.split("-")[1];
//        System.out.println(month);
//        //设置时间戳
//        Timestamp timestamp = new Timestamp(date.getTime());
//        //找到最近一个月内的所有文件
//        List<Fil> fils = filService.getFilByMonth(timestamp);
//        //设置存储文件夹ID和对应路径的Map
//        Map<Integer,String> wayMap=new HashMap();
//        //将所有文件夹id和对应路径存入
//        folderService.allContent(wayMap);
//        //将所有文件的名字和文件存好
//        List<String[]> filNameAndWay = filNameAndWay(fils,wayMap);
//        //设置存储地址
//        String zipBasePath = "D:/couldriver";
//        String zipName = yearMonth+"备份.zip";
//        String zipFilePath = zipBasePath +File.separator+year;
//        File zip = new File(zipFilePath);
//        if (!zip.exists()) {
//            zip.mkdir();
//        }
//        zipFilePath = zipFilePath +File.separator + month;
//        zip = new File(zipFilePath);
//        if (!zip.exists()) {
//            zip.mkdir();
//        }
//        zipFilePath = zipFilePath +File.separator + zipName;
//        //压缩文件的-创建
//         zip = new File(zipFilePath);
//        if (!zip.exists()) {
//            zip.createNewFile();
//        }
//        try {
//            ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zip));
//            ZipUtil.zipFile(filNameAndWay, zos); //调用util工具创建
//            zos.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//    private static List<String[]> filNameAndWay(List<Fil> fils,Map<Integer,String> wayMap){
//        List <String[]> filNameAndWay = new ArrayList<>();
//        for (Fil fil : fils) {
//            //如果父文件夹为null，则跳过
//            if(wayMap.get(fil.getFol_id())==null)
//                continue;
//            filNameAndWay.add(new String[]{wayMap.get(fil.getFol_id())+fil.getName(),fil.getWay()});
//        }
//        return filNameAndWay;
    }
}
