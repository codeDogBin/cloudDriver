package com.bin.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.sql.Timestamp;
@Setter
@ToString
@Getter
public class User implements Serializable {
    private Integer id;
    private  String name;
    private String password;
    private String tel;
    private  String address;
    private  Timestamp ctime;
    private  Timestamp last_login;
    private  Integer count;
    private  Integer Permission_id;
}
