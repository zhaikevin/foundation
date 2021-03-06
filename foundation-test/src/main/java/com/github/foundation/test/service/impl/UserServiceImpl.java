package com.github.foundation.test.service.impl;

import com.github.foundation.service.BaseServiceImpl;
import com.github.foundation.test.dao.UserMapper;
import com.github.foundation.test.model.User;
import com.github.foundation.test.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

/**
 * @Description:
 * @Author: kevin
 * @Date: 2019/11/8 09:52
 */
@Service
public class UserServiceImpl extends BaseServiceImpl<User, UserMapper> implements UserService {

    @Autowired
    private UserMapper userMapper;

//    @Override
//    @Pageable
//    public void queryByPage(Pagination<User> pagination) {
//        List<User> list = userMapper.selectAll();
//        pagination.setDataset(list);
//        pagination.setTotal(((Page<User>) list).getTotal());
//    }

    @Override
    public User getByName(String userName) {
        Example example = new Example(User.class);
        example.createCriteria().andEqualTo("name", userName);
        return userMapper.selectOneByExample(example);
    }
}
