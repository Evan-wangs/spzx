package com.atguigu.spzx.manager.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Data
@ConfigurationProperties(prefix = "spzx.auth")
public class UserProperties {

    // 获取配置文件中的 spzx.auth.noAuthUrls
    private List<String> noAuthUrls;
}
