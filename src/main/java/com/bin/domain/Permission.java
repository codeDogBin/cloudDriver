package com.bin.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.sql.Timestamp;

@Setter
@Getter
@ToString
public class Permission implements Serializable {
    private Integer id;
    private String name;
    private boolean readall;
    private  boolean upload;
    private boolean down;
    private  boolean modify;
    private boolean del;
    private Timestamp ctime;
}
