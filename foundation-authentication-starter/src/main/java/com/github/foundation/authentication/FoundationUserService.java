package com.github.foundation.authentication;

import com.github.foundation.authentication.model.FoundationUser;

/**
 * @Description:
 * @Author: kevin
 * @Date: 2019/11/18 14:36
 */
public interface FoundationUserService {

    /**
     * 根据用户名获取用户信息
     * @param userName
     * @return
     */
    FoundationUser getByName(String userName);
}
