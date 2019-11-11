package com.github.foundation.test.service;

import com.github.foundation.pagination.model.Pagination;
import com.github.foundation.test.model.User;

/**
 * @Description:
 * @Author: kevin
 * @Date: 2019/11/8 09:51
 */
public interface UserService {

    void queryByPage(Pagination<User> pagination);
}
