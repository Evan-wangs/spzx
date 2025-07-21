package com.atguigu.spzx.utils;

import com.atguigu.spzx.model.entity.system.SysUser;

/*
*   操作线程变量
* */
public class AuthContextUtil {

    // 创建一个threadLocal对象，用于存储当前线程的用户ID
    private static final ThreadLocal<SysUser> threadLocal = new ThreadLocal<>();

    // 添加数据
    public static void set(SysUser sysUser) {
        threadLocal.set(sysUser);
    }

    // 获取数据
    public static SysUser get() {
        return threadLocal.get();
    }

    // 删除数据
    public static void remove() {
        // 请求结束后要及时清理，防止内存泄漏
        threadLocal.remove();
    }
}
