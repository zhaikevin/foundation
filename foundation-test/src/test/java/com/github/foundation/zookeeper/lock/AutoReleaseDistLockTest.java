package com.github.foundation.zookeeper.lock;

import com.github.foundation.common.utils.ThreadUtils;
import com.github.foundation.zookeeper.ZookeeperTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * @Description: 基于zookeeper的可以自动释放的分布式锁测试类
 * @Author: kevin
 * @Date: 2019/7/2 14:49
 */
@Slf4j
public class AutoReleaseDistLockTest extends ZookeeperTest {

    @Test
    public void testLock() throws InterruptedException {
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                try (AutoReleaseDistLock lock = new AutoReleaseDistLock("dummy")) {
                    boolean flag = lock.lock();
                    log.info("thread {} ,lock flag:{}", Thread.currentThread().getName(), flag);
                    ThreadUtils.sleepQuietly(5 * 1000);
                } catch (Exception e) {
                    log.error("some exception", e);
                }
            }).start();
        }

        Thread.currentThread().join(30 * 1000);
    }

}
