package com.github.foundation.authentication;

import com.github.foundation.authentication.model.FoundationUser;
import com.github.foundation.common.utils.MD5Utils;
import com.github.foundation.common.utils.ValidateUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Component;

/**
 * @Description:
 * @Author: kevin
 * @Date: 2019/11/19 10:43
 */
@Component
public class AuthenticationManager {

    /**
     * 登录
     * @param userName
     * @param password
     */
    public void login(String userName, String password) {
        ValidateUtils.notEmptyString(userName, "用户名不能为空");
        ValidateUtils.notEmptyString(password, "密码不能为空");
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(userName, password);
        subject.login(token);
    }

    /**
     * 登出
     */
    public void logout() {
        SecurityUtils.getSubject().logout();
    }

    /**
     * 获取用户id
     * @return
     */
    public Long getUserId() {
        return ((FoundationUser) SecurityUtils.getSubject().getPrincipal()).getUserId();
    }

    /**
     * 获取用户名
     * @return
     */
    public String getUserName() {
        return ((FoundationUser) SecurityUtils.getSubject().getPrincipal()).getUserName();
    }

    /**
     * 对密码进行加密
     * @param userName 用户名
     * @param password 密码
     * @param salt     密码盐
     */
    public String encrypt(String userName, String password, String salt) throws Exception {
        return MD5Utils.encrypt(password, userName, salt);
    }
}
