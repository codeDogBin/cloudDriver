package com.bin;

import com.bin.domain.Company;
import com.bin.service.CompanyService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.sql.Timestamp;

public class test {
    private static ApplicationContext apc;
    static{ apc = new ClassPathXmlApplicationContext("applicationContext.xml"); }

    public static void main(String[] args) {
        CompanyService cs = apc.getBean("companyService",CompanyService.class);
        Company company = new Company();
        company.setName("彬彬");
        company.setWay("123123");
        company.setCtime(new Timestamp(System.currentTimeMillis()));
        cs.registerCompany(company);
        System.out.println(company.getId());
    }

}
