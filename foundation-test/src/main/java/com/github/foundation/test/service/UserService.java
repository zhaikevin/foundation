package com.github.foundation.test.service;

import com.github.foundation.pagination.model.Pagination;
import com.github.foundation.service.BaseService;
import com.github.foundation.test.dao.UserMapper;
import com.github.foundation.test.model.User;

/**
 * @Description:
 * @Author: kevin
 * @Date: 2019/11/8 09:51
 */
public interface UserService extends BaseService<User, UserMapper> {

    //void queryByPage(Pagination<User> pagination);

    User getByName(String userName);
}
