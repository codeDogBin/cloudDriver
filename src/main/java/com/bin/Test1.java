package com.bin;

import com.bin.util.TimeUtil;

import java.util.Arrays;

public class Test1 {
    public static void main(String[] args) {
        String[] lastMonth = TimeUtil.getLastMonth();
        System.out.println(Arrays.toString(lastMonth));
        System.out.println(TimeUtil.getThisTime());
    }
}
