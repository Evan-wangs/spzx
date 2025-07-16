package com.atguigu.spzx.manager.service.impl;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.CircleCaptcha;
import com.atguigu.spzx.manager.service.ValidateCodeService;
import com.atguigu.spzx.model.vo.system.ValidateCodeVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class ValidateCodeServiceImpl implements ValidateCodeService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public ValidateCodeVo generateValidateCode() {
        // 1. 通过工具 hutool 生成图片验证码
        CircleCaptcha captcha = CaptchaUtil.createCircleCaptcha(150,48, 4, 5); // int width, int height, int codeCount 验证码位数, int circleCount 干扰线的数量
        String codeValue = captcha.getCode(); // 生成的验证码
        String imageBase64 = captcha.getImageBase64(); // 对图片进行了base64编码

        // 2. 把验证码存储到redis中，并设置redis的key和value（验证码值），并设立数据过期时间
        String key = UUID.randomUUID().toString().replaceAll("-", "");
        redisTemplate.opsForValue().set("user:validate" + key, codeValue, 5, TimeUnit.MINUTES);

        // 3. 返回ValidateCodeVo对象
        ValidateCodeVo validateCodeVo = new ValidateCodeVo();
        validateCodeVo.setCodeKey(key);
        validateCodeVo.setCodeValue("data:image/png;base64," + imageBase64);
        return validateCodeVo;
    }
}
