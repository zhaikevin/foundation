package com.github.foundation.zookeeper.lock;

import com.github.foundation.spring.SpringContextUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;

import java.util.concurrent.TimeUnit;

/**
 * @Description: 基于zookeeper的可以自动释放的分布式锁
 * @Author: kevin
 * @Date: 2019/7/2 14:43
 */
@Slf4j
public class AutoReleaseDistLock implements AutoCloseable {

    /**
     * 缓存时间，30秒
     */
    private static final int EXPIRE_TIME_SECONDS = 30;

    /**
     * client
     */
    private CuratorFramework client;

    /**
     * 锁的唯一名。通常为锁定目标的code.
     */
    private String locker;

    /**
     * 锁获取标志。true为已经获取到锁
     */
    private Boolean lockFlag;

    /**
     * mutex
     */
    private InterProcessMutex mutex;

    /**
     * @param locker
     */
    public AutoReleaseDistLock(String locker) {
        super();
        this.locker = locker;
        this.client = SpringContextUtils.getBean(CuratorFramework.class);
    }

    @Override
    public void close() {
        try {
            if (lockFlag) {
                mutex.release();
            }
        } catch (Exception e) {
            log.error("release lock exception, but it can be ignored.", e);
        }
    }

    /**
     * 锁方法
     * @return 是否获得锁
     */
    public boolean lock() {
        try {
            String zkPath = "/lock/" + locker;
            this.mutex = new InterProcessMutex(client, zkPath);
            lockFlag = mutex.acquire(EXPIRE_TIME_SECONDS, TimeUnit.SECONDS);
            return lockFlag;
        } catch (Exception e) {
            log.error("acquire lock exception.", e);
            return false;
        }
    }
}
