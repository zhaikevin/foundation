package com.github.foundation.test.service.impl;

import com.github.foundation.pagination.annotation.Pageable;
import com.github.foundation.pagination.model.Pagination;
import com.github.foundation.test.dao.UserMapper;
import com.github.foundation.test.model.User;
import com.github.foundation.test.service.UserService;
import com.github.pagehelper.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description:
 * @Author: kevin
 * @Date: 2019/11/8 09:52
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    @Pageable
    public void queryByPage(Pagination<User> pagination) {
        List<User> list = userMapper.selectAll();
        pagination.setDataset(list);
        pagination.setTotal(((Page<User>) list).getTotal());
    }
}
