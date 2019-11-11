package com.github.foundation.redis;

import com.github.foundation.SpringBootTestAbstract;
import com.github.foundation.common.utils.ThreadUtils;
import com.github.foundation.test.model.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description:
 * @Author: kevin
 * @Date: 2019/11/5 16:30
 */
public class RedisManagerTest extends SpringBootTestAbstract {

    @Autowired
    private RedisManager redisManager;

    private String key1 = "test1";

    private String key2 = "test2";

    @Before
    public void prepare() {
        redisManager.delete(key1);
        redisManager.delete(key2);
    }

    private User mockData() {
        User user = new User();
        user.setId(1L);
        user.setName("kevin");
        user.setSex(0);
        return user;
    }

    private void validate(User user) {
        Assert.assertEquals((Long) 1L, user.getId());
        Assert.assertEquals("kevin", user.getName());
        Assert.assertEquals(0, user.getSex());
    }

    @Test
    public void setTest() {
        User user = mockData();
        redisManager.set(key1, user);
        user = redisManager.get(key1);
        validate(user);
    }

    @Test
    public void setExpireTest() {
        User user = mockData();
        redisManager.setExpire(key2, user, 10);
        user = redisManager.get(key2);
        validate(user);
        ThreadUtils.sleepQuietly(10000);
        user = redisManager.get(key2);
        Assert.assertNull(user);
    }
}
