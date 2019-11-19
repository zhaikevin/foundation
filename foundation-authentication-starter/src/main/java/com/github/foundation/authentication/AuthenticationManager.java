package com.github.foundation.authentication;

import com.github.foundation.common.utils.ValidateUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;

/**
 * @Description:
 * @Author: kevin
 * @Date: 2019/11/19 10:43
 */
public final class AuthenticationManager {

    /**
     * 登录
     * @param userName
     * @param password
     */
    public static void login(String userName, String password) {
        ValidateUtils.notEmptyString(userName, "用户名不能为空");
        ValidateUtils.notEmptyString(password, "密码不能为空");
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(userName, password);
        subject.login(token);
    }
}
