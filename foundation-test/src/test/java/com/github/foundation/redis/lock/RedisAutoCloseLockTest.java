package com.github.foundation.redis.lock;

import com.github.foundation.SpringBootTestAbstract;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * @Description: 基于redis的可以自动释放的分布式锁测试类
 * @Author: kevin
 * @Date: 2019/7/2 14:49
 */
@Slf4j
public class RedisAutoCloseLockTest extends SpringBootTestAbstract {

    @Test
    public void testLock() throws InterruptedException {
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                try (RedisAutoCloseLock lock = new RedisAutoCloseLock("dummy")) {
                    boolean flag = lock.lock(5);
                    log.info("thread {} ,lock flag:{}", Thread.currentThread().getName(), flag);
                } catch (Exception e) {
                    log.error("some exception", e);
                }
            }).start();
        }
        Thread.currentThread().join(5 * 1000);
    }

}
