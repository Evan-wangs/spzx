package com.atguigu.spzx.manager.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.atguigu.spzx.common.exception.GuiguException;
import com.atguigu.spzx.manager.mapper.SysUserMapper;
import com.atguigu.spzx.manager.service.SysUserService;
import com.atguigu.spzx.model.dto.system.LoginDto;
import com.atguigu.spzx.model.entity.system.SysUser;
import com.atguigu.spzx.model.vo.common.ResultCodeEnum;
import com.atguigu.spzx.model.vo.system.LoginVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class SysUserServiceImpl implements SysUserService {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public LoginVo login(LoginDto loginDto) {

        // 验证码校验
        String captcha = loginDto.getCaptcha(); // 获取输入的4位验证码
        String key = loginDto.getCodeKey(); // redis 中存储的key
        String resultCode = redisTemplate.opsForValue().get("user:validate" + key);
        if (StrUtil.isEmpty(resultCode) || !StrUtil.equalsIgnoreCase(resultCode, captcha)) {
            throw new GuiguException(ResultCodeEnum.VALIDATE_CODE_ERROR);
        }
        redisTemplate.delete("user:validate" + key); // 验证通过时，清空redis中的验证码

        // 1. 获取提交的用户名
        String userName = loginDto.getUserName();

        // 2. 查询sys_user表，用户存在check，用户不存在，返回登录失败
        SysUser sysUser = sysUserMapper.selectUserInfoByUserName(userName);
        if (sysUser == null) {
            throw new GuiguException(ResultCodeEnum.LOGIN_ERROR);
        }

        // 3. 密码正确check 输入的密码加密后，进行比较
        String databasePassword = sysUser.getPassword();
        String inputPassword = DigestUtils.md5DigestAsHex(loginDto.getPassword().getBytes());

        if(!inputPassword.equals(databasePassword)){
            throw new GuiguException(ResultCodeEnum.LOGIN_ERROR);
        }

        // 4. 登录成功，生成用户唯一标识token
        String token = UUID.randomUUID().toString().replace("-", "");

        // 5. 把登录成功用户信息登录到redis中，并设置好redis的过期时间的配置信息
        // key: token value: 用户信息 timeout, timeoutUnit
        redisTemplate.opsForValue().set("user:login" + token, JSON.toJSONString(sysUser), 7, TimeUnit.DAYS);

        // 6. 返回loginVo对象（存放token）
        LoginVo loginVo = new LoginVo();
        loginVo.setToken(token);

        return loginVo;
    }

    // 获取当前登录用户的信息
    public SysUser getUserInfo(String token) {
        String userJson = redisTemplate.opsForValue().get("user:login" + token);
        return JSON.parseObject(userJson, SysUser.class);
    }

    // 用户退出
    public void logout(String token) {
        redisTemplate.delete("user:login" + token);
    }
}
