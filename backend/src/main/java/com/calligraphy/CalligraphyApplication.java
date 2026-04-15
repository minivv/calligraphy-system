package com.calligraphy;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.calligraphy.mapper")
public class CalligraphyApplication {
    public static void main(String[] args) {
        SpringApplication.run(CalligraphyApplication.class, args);
    }
}
