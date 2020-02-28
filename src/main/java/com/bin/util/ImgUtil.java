package com.bin.util;

import com.bin.domain.Fil;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Component;
import java.io.File;
import java.io.IOException;
import java.util.List;
@Component
public class ImgUtil {
    private static String[] suffixes=new String[]{"jpg","JPG","png","PNG","jpeg","bmp"};
    public static boolean isImg(String suffix){
        for (String s : suffixes) {
            if(s.equals(suffix))
                return true;
        }
        return false;
    }
    public static List<Fil> getImg(List<Fil> fils, String way) throws IOException {
            for (Fil fil : fils) {
                if(fil.getImgWay()==null){
                createImg(fil,way);
                }
            }
            return fils;
        }


    public static void createImg(Fil fil, String way) throws IOException {
        String suffix = fil.getName().substring(fil.getName().lastIndexOf(".")+1);
        if(isImg(suffix)){//判断是否为照片文件
            String imgWay =way+File.separator+"img"+File.separator+fil.getFol_id()+File.separator+fil.getName();
            File newfile = new File(imgWay);
            if(!newfile.exists()){
                File file = new File(fil.getWay());
                if(file.length()!=newfile.length())
                    FileUtils.copyFile(file,newfile);

            }
            imgWay="img"+File.separator+fil.getFol_id()+File.separator+fil.getName();
            fil.setImgWay(imgWay);
        }else if("doc".equals(suffix)||"docx".equals(suffix)){
            fil.setImgWay("img/doc.jpg");
        } else if("pdf".equals(suffix)){
            fil.setImgWay("img/pdf.jpg");
        } else{
            fil.setImgWay("img/file.jpg");
        }
    }

}
