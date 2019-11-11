package com.github.foundation.service;

import com.github.foundation.SpringBootTestAbstract;
import com.github.foundation.pagination.model.Direction;
import com.github.foundation.pagination.model.Order;
import com.github.foundation.pagination.model.Pagination;
import com.github.foundation.pagination.model.Sort;
import com.github.foundation.test.model.User;
import com.github.foundation.test.service.UserService;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description:
 * @Author: kevin
 * @Date: 2019/11/8 14:59
 */
public class UserServiceTest extends SpringBootTestAbstract {

    @Autowired
    private UserService userService;

    @Test
    public void queryByPageTest() {
        Sort sortObj = new Sort(new Order(Direction.fromString("desc"), "id"));
        Pagination<User> pagination = new Pagination<>(1, 10, sortObj);
        userService.queryByPage(pagination);
        Assert.assertEquals(10, pagination.getDataset().size());
        Assert.assertEquals(15, pagination.getTotal());
    }

}
