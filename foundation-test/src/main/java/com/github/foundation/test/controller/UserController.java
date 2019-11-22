package com.github.foundation.test.controller;

import com.github.foundation.authentication.AuthenticationManager;
import com.github.foundation.common.model.ResultInfo;
import com.github.foundation.test.model.User;
import com.github.foundation.test.service.UserService;
import org.apache.shiro.authc.AuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description:
 * @Author: kevin
 * @Date: 2019/11/19 14:27
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResultInfo login(@RequestBody User user) {
        try {
            authenticationManager.login(user.getName(), user.getPassword());
        } catch (AuthenticationException e) {
            return ResultInfo.errorMessage(e.getMessage());
        }
        return ResultInfo.success();
    }

    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public ResultInfo logout() {
        authenticationManager.logout();
        return ResultInfo.success();
    }

    @RequestMapping(value = "/getByName", method = RequestMethod.GET)
    public ResultInfo getByName(@RequestParam(value = "name") String name) {
        return ResultInfo.success(userService.getByName(name));
    }
}
