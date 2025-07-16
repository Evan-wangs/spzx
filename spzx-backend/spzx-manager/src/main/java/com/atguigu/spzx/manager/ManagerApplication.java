package com.atguigu.spzx.manager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.atguigu.spzx"}) // 修改包的扫描路径规则
public class ManagerApplication {

    public static void main(String[] args){
        SpringApplication.run(ManagerApplication.class, args);
    }
}
