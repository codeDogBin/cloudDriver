package com.bin.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeUtil {
    public static String[]  getLastMonth() {
        //将时间转化为
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
        String yearMonth = format.format(getLastTime());
        String year = yearMonth.split("-")[0];
        String month = yearMonth.split("-")[1];
        String[] lastMonth = new String[]{yearMonth,year,month};
        return lastMonth;
    }
    //得到时间是上个月的1号凌晨1点
    public static Date getLastTime(){

        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - 1);
        setOne(calendar);
        return calendar.getTime();
    }
    //得到时间是这个月的1号凌晨1点
    public static Date getThisTime(){
        Calendar calendar = Calendar.getInstance();
        setOne(calendar);
        System.out.println(calendar.getTime());
        return calendar.getTime();
    }
    //将时间改为1号凌晨1点
    private static void setOne(Calendar calendar){
        calendar.set(Calendar.DAY_OF_MONTH,1);
        calendar.set(Calendar.HOUR_OF_DAY,1);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
    }
}
