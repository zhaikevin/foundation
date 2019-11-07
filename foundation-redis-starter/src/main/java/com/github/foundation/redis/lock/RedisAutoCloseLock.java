package com.github.foundation.redis.lock;

import com.github.foundation.redis.RedisManager;
import com.github.foundation.spring.ProjectConfig;
import com.github.foundation.spring.SpringContextUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * 基于redis的自动释放锁
 */
@Slf4j
public class RedisAutoCloseLock implements AutoCloseable {

    /**
     * 缓存时间，30秒
     */
    private static final int DEFAULT_EXPIRE_TIME_SECONDS = 30;

    /**
     * key前缀
     */
    private static final String LOCK_KEY_PREFIX = "-lock-";

    /**
     * 锁对象
     */
    private Object lock;

    /**
     * 环境
     */
    private String env;

    /**
     * 标志
     */
    private Boolean lockFlag;

    /**
     * manger
     */
    private RedisManager manager;

    public RedisAutoCloseLock(Object lock) {
        this.lock = lock;
        this.env = SpringContextUtils.getBean(ProjectConfig.class).getEnv();
        this.lockFlag = false;
        this.manager = SpringContextUtils.getBean(RedisManager.class);
    }

    /**
     * 锁
     * @return
     */
    public Boolean lock() {
        return lock(DEFAULT_EXPIRE_TIME_SECONDS);
    }

    /**
     * 锁
     * @param seconds 过期时间
     * @return 锁结果
     */
    public Boolean lock(int seconds) {
        lockFlag = manager.setIfAbsent(composeKey(),
                seconds, seconds);
        return lockFlag;
    }

    /**
     * 是否加锁成功
     * @return
     */
    public Boolean isLocked() {
        return lockFlag;
    }

    /**
     * 锁是否已经被别人给释放了
     * @return
     */
    public Boolean isClosedByOther() {
        return manager.isExist(composeKey());
    }

    /**
     * 释放锁
     */
    public void unlock() {
        this.close();
    }

    @Override
    public void close() {
        try {
            manager.delete(composeKey());
            lockFlag = false;
        } catch (Exception e) {
            log.error("close lock failed:{}", e.getMessage(), e);
        }
    }

    /**
     * 组装key
     * @return
     */
    private String composeKey() {
        return env + LOCK_KEY_PREFIX + lock;
    }
}
