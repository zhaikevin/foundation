package com.github.foundation.authentication.model;

import lombok.Data;

/**
 * @Description:
 * @Author: kevin
 * @Date: 2019/11/18 14:40
 */
@Data
public class FoundationUser {

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 加密后的密码
     */
    private String password;

    /**
     * 是否有效
     */
    private Boolean isValid;

    /**
     * 密码盐
     */
    private String userSalt;

}
