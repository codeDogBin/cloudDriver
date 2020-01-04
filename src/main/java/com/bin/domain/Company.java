package com.bin.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;

@Setter
@Getter
@ToString
public class Company implements Serializable {
    private Integer id;
    private String name;
    private String way;
    private Timestamp ctime;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Company company = (Company) o;
        return Objects.equals(id, company.id) &&
                Objects.equals(name, company.name) &&
                Objects.equals(way, company.way) &&
                Objects.equals(ctime, company.ctime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, way, ctime);
    }
}
