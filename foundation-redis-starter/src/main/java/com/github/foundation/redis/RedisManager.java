package com.github.foundation.redis;

import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @Description: redis管理器
 * @Author: kevin
 * @Date: 2019/11/5 16:08
 */
@Component
@Slf4j
public class RedisManager {

    @Autowired
    private RedisTemplate redisTemplate;

    public void setExpire(String key, Object value, long second) {
        Preconditions.checkNotNull(key);
        Preconditions.checkNotNull(value);
        redisTemplate.opsForValue().set(key, value, second, TimeUnit.SECONDS);
    }

    public void set(String key, Object value) {
        Preconditions.checkNotNull(key);
        Preconditions.checkNotNull(value);
        redisTemplate.opsForValue().set(key, value);
    }

    public Boolean setIfAbsent(String key, Object value, long second) {
        Preconditions.checkNotNull(key);
        Preconditions.checkNotNull(value);
        return redisTemplate.opsForValue().setIfAbsent(key, value, second, TimeUnit.SECONDS);
    }

    public <V> V get(String key) {
        Preconditions.checkNotNull(key);
        Object value = redisTemplate.opsForValue().get(key);
        if (value == null) {
            return null;
        }
        return (V) value;
    }

    /**
     * 删除
     * @param key
     */
    public void delete(String key) {
        Preconditions.checkNotNull(key);
        redisTemplate.delete(key);
    }

    /**
     * 是否存在
     * @param key
     * @return
     */
    public Boolean isExist(String key) {
        Preconditions.checkNotNull(key);
        return redisTemplate.hasKey(key);
    }
}
