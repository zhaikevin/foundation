package com.github.foundation.zookeeper.election;

import com.github.foundation.zookeeper.ZookeeperTest;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.recipes.leader.LeaderLatchListener;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 选主操作测试
 * @Author: kevin
 * @Date: 2019/7/4 15:44
 */
@Slf4j
public class ElectionCommitteeTest extends ZookeeperTest {

    @Autowired
    private ElectionCommittee electionCommittee;

    @Test
    public void testStartLeaderLatch() throws InterruptedException {

        electionCommittee.startLeaderLatch("test-leader", new LeaderLatchListener() {

            @Override
            public void notLeader() {
                log.info("I am not leader");
            }

            @Override
            public void isLeader() {
                log.info("I am leader");
            }
        });

        Thread.currentThread().join(5000);
    }
}
