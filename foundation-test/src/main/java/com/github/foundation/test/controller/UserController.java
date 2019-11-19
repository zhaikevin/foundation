package com.github.foundation.test.controller;

import com.github.foundation.authentication.AuthenticationManager;
import com.github.foundation.common.model.ResultInfo;
import com.github.foundation.test.model.User;
import org.apache.shiro.authc.AuthenticationException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description:
 * @Author: kevin
 * @Date: 2019/11/19 14:27
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResultInfo login(@RequestBody User user) {
        try {
            AuthenticationManager.login(user.getName(), user.getPassword());
        } catch (AuthenticationException e) {
            return ResultInfo.errorMessage(e.getMessage());
        }
        return ResultInfo.success();
    }
}
