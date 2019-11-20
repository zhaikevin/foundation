package com.github.foundation.authentication.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @Description:
 * @Author: kevin
 * @Date: 2019/11/18 14:40
 */
@Data
public class FoundationUser implements Serializable {

    private static final long serialVersionUID = -5947182775784006019L;
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
