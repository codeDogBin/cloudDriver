package com.bin.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.sql.Timestamp;

@Setter
@Getter
@ToString
public class User_Company implements Serializable {
    private  Integer id;
    private  Integer user_id;
    private  Integer company_id;
    private  Timestamp ctime;
}
