package com.atguigu.spzx.manager;

import com.atguigu.spzx.manager.properties.UserProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.atguigu.spzx"}) // 修改包的扫描路径规则
@EnableConfigurationProperties(value = {UserProperties.class}) // 启用配置属性类
public class ManagerApplication {

    public static void main(String[] args){
        SpringApplication.run(ManagerApplication.class, args);
    }
}
