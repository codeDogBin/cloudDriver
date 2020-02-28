package com.bin.util;

public enum  StateUtil {
    OK("OK"),
    Fail("FAIL");
    private  String value;
    StateUtil(String value){this.value = value;}
    public  String getValue(){return value;}
}
