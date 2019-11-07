package com.github.foundation.zookeeper;

import com.github.foundation.SpringBootTestAbstract;
import org.apache.curator.framework.CuratorFramework;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @Description:
 * @Author: kevin
 * @Date: 2019/11/4 14:41
 */
public class ZookeeperTest extends SpringBootTestAbstract {

    @Autowired
    private CuratorFramework client;

    @Before
    public void prepare() throws Exception {

        List<String> children = client.getChildren().forPath("/");
        for (String child : children) {
            client.delete().deletingChildrenIfNeeded().forPath("/" + child);
        }
    }
}
