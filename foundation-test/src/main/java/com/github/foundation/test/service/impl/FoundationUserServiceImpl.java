package com.github.foundation.test.service.impl;

import com.github.foundation.authentication.FoundationUserService;
import com.github.foundation.authentication.model.FoundationUser;
import com.github.foundation.test.model.User;
import com.github.foundation.test.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description:
 * @Author: kevin
 * @Date: 2019/11/18 17:11
 */
@Service
public class FoundationUserServiceImpl implements FoundationUserService {

    @Autowired
    private UserService userService;

    @Override
    public FoundationUser getByName(String userName) {
        FoundationUser foundationUser = new FoundationUser();
        User user = userService.getByName(userName);
        if (user == null) {
            return null;
        }
        foundationUser.setUserName(user.getName());
        foundationUser.setUserId(user.getId());
        foundationUser.setPassword(user.getPassword());
        foundationUser.setUserSalt(user.getSalt());
        foundationUser.setIsValid(user.getStatus() == 1);
        return foundationUser;
    }
}
