package com.test.test2;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @program: FBS-Transaction
 * @description:
 * @author: 翟飞
 * @create: 2019-07-30 23:28
 **/
@SpringBootApplication
@MapperScan(basePackages = "com.test.test2")
public class Test2Application {
    public static void main(String[] args) {
        SpringApplication.run(Test2Application.class,args);
    }
}
    