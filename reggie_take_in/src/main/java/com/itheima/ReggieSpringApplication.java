package com.itheima;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@ServletComponentScan
@SpringBootApplication
public class ReggieSpringApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReggieSpringApplication.class,args);
    }
}
