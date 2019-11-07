package com.github.foundation.spring;

import com.github.foundation.SpringBootTestAbstract;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description:
 * @Author: kevin
 * @Date: 2019/11/6 17:24
 */
@Slf4j
public class SpringContextUtilsTest extends SpringBootTestAbstract {

    @Autowired
    private ProjectConfig config;

    @Test
    public void projectConfigTest() {
        Assert.assertEquals("foundation-unittest",config.getName());
        Assert.assertEquals("unit",config.getEnv());
    }
}
