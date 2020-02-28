package com.bin.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.sql.Timestamp;
@Setter
@Getter
@ToString
public class Fil implements Serializable {
    private Integer id;
    private String name;
    private String way;
    private String imgWay;
    private Integer fol_id;
    private Timestamp ctime;
    private boolean state;
    private Timestamp del_time;
}
