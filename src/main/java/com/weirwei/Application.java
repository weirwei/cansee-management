package com.weirwei;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

/**
 * @author weirwei 2021/3/15 17:43
 */
@SpringBootApplication
@ServletComponentScan("com.weirwei.cansee.filter")
@MapperScan("com.weirwei.cansee.mapper")
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}

