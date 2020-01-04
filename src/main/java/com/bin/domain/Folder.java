package com.bin.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.sql.Timestamp;
@Getter
@Setter
@ToString
public class Folder implements Serializable {
    private Integer id;
    private String name;
    private String way;
    private Integer fway_id;
    private Timestamp ctime;
    private Integer company_id;
    private boolean state;
}
