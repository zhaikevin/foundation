package com.github.foundation.authentication.config;

import com.github.foundation.authentication.shiro.FoundationRealm;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @Description:
 * @Author: kevin
 * @Date: 2019/11/18 15:59
 */
@Configuration
public class ShiroConfig {

    @Value("${foundation.authentication.sessionTimeoutSeconds:1800}")
    private Long timeout;

//    @Value("${spring.redis.host}")
//    private String host;
//
//    @Value("${spring.redis.port}")
//    private int port;
//
//    @Value("${spring.redis.password}")
//    private String password;

    @Value("${foundation.authentication.loginUrl}")
    private String loginUrl;

    @Value("${foundation.authentication.unauthorizedUrl}")
    private String unauthorizedUrl;

    @Value("${foundation.authentication.excludeUrls}")
    private String excludeUrls;

    @Bean("shiroFilter")
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilter = new ShiroFilterFactoryBean();
        shiroFilter.setSecurityManager(securityManager);
        shiroFilter.setLoginUrl(loginUrl);
        shiroFilter.setUnauthorizedUrl(unauthorizedUrl);
        Map<String, String> filterMap = new LinkedHashMap<>();
        if (StringUtils.isNotEmpty(excludeUrls)) {
            String[] permissionUrl = excludeUrls.split(",");
            for (String url : permissionUrl) {
                filterMap.put(url, "anon");
            }
        }
        filterMap.put("/**", "authc");
        shiroFilter.setFilterChainDefinitionMap(filterMap);
        return shiroFilter;
    }

    @Bean("securityManager")
    public SecurityManager securityManager(FoundationRealm foundationRealm, SessionManager sessionManager) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(foundationRealm);
        securityManager.setSessionManager(sessionManager);
        return securityManager;
    }

    /**
     * 单机session
     * @return
     */
    @Bean
    public DefaultWebSessionManager sessionManager() {
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        sessionManager.setSessionValidationSchedulerEnabled(true);
        sessionManager.setSessionIdUrlRewritingEnabled(false);
        sessionManager.setSessionValidationInterval(timeout * 1000);
        sessionManager.setGlobalSessionTimeout(timeout * 1000);
        return sessionManager;
    }

    /**
     * 基于redis的分布式session
     * @param securityManager
     * @return
     */
//    @Bean("securityManager")
//    public SecurityManager securityManager(FoundationRealm foundationRealm, SessionManager sessionManager, RedisCacheManager cacheManager) {
//        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
//        securityManager.setRealm(foundationRealm);
//        securityManager.setSessionManager(sessionManager);
//        securityManager.setCacheManager(cacheManager);
//        return securityManager;
//    }


//    @Bean
//    public SessionManager sessionManager(RedisSessionDAO redisSessionDAO) {
//        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
//        //设置session超时时间
//        sessionManager.setGlobalSessionTimeout(timeout * 10000);
//        //设置redisSessionDao
//        sessionManager.setSessionDAO(redisSessionDAO);
//        return sessionManager;
//    }

//    @Bean
//    public RedisSessionDAO redisSessionDAO(RedisManager redisManager) {
//        RedisSessionDAO redisSessionDAO = new RedisSessionDAO();
//        redisSessionDAO.setRedisManager(redisManager);
//        return redisSessionDAO;
//    }

//    @Bean("shiroRedisManager")
//    public RedisManager redisManager() {
//        RedisManager redisManager = new RedisManager();
//        redisManager.setHost(host);
//        redisManager.setPort(port);
//        if (StringUtils.isNotEmpty(password)) {
//            redisManager.setPassword(password);
//        }
//        return redisManager;
//    }

//    @Bean
//    public RedisCacheManager cacheManager(RedisManager redisManager) {
//        RedisCacheManager redisCacheManager = new RedisCacheManager();
//        redisCacheManager.setRedisManager(redisManager);
//        redisCacheManager.setExpire(timeout.intValue());
//        return redisCacheManager;
//    }
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }

    @Bean
    @DependsOn("lifecycleBeanPostProcessor")
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        defaultAdvisorAutoProxyCreator.setProxyTargetClass(true);
        return defaultAdvisorAutoProxyCreator;
    }

    @Bean
    public static LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }
}
