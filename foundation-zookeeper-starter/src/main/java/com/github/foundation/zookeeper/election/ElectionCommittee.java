package com.github.foundation.zookeeper.election;

import com.github.foundation.common.exception.ZKOperationException;
import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.leader.LeaderLatch;
import org.apache.curator.framework.recipes.leader.LeaderLatchListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Description: 选举
 * @Author: kevin
 * @Date: 2019/11/1 14:50
 */
@Slf4j
@Component
public class ElectionCommittee {

    @Autowired
    private CuratorFramework client;

    /**
     * 选主
     * @param leaderName leader name
     * @param listener   listener
     * @return leader node的zk path
     */
    public String startLeaderLatch(String leaderName, LeaderLatchListener listener) {
        Preconditions.checkArgument(StringUtils.isNotBlank(leaderName));
        String zkPath = "/leader/" + leaderName;
        try {
            LeaderLatch leader = new LeaderLatch(client, zkPath);
            leader.addListener(listener);
            leader.start();
            return zkPath;
        } catch (Exception e) {
            log.error("election leader exception", e);
            throw new ZKOperationException(e.getMessage());
        }
    }
}
