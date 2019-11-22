package com.github.foundation.authentication.shiro;

import com.github.foundation.authentication.FoundationUserService;
import com.github.foundation.authentication.model.FoundationUser;
import com.github.foundation.authentication.utils.MD5Utils;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.AllowAllCredentialsMatcher;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * @Description:
 * @Author: kevin
 * @Date: 2019/11/18 14:18
 */
@Component
@Slf4j
public class FoundationRealm extends AuthorizingRealm {

    @Autowired
    private FoundationUserService foundationUserService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        FoundationUser user = (FoundationUser) principals.getPrimaryPrincipal();
        Set<String> permissions = foundationUserService.getPermissions(user.getUserId());
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.setStringPermissions(permissions);
        return info;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        FoundationUser user = foundationUserService.getByName(token.getUsername());
        if (user == null) {
            throw new AuthenticationException("用户不存在");
        }
        try {
            if (!MD5Utils.encrypt(String.valueOf(token.getPassword()), token.getUsername(), user.getUserSalt()).equals(user.getPassword())) {
                throw new AuthenticationException("密码不正确");
            }
        } catch (Exception e) {
            if (!(e instanceof AuthenticationException)) {
                log.error("encrypt password failed:{}", e.getMessage(), e);
            }
            throw new AuthenticationException(e.getMessage());
        }
        if (!user.getIsValid()) {
            throw new AuthenticationException("账户已被锁定");
        }
        return new SimpleAuthenticationInfo(user, user.getPassword(), getName());
    }

    @Override
    public void setCredentialsMatcher(CredentialsMatcher credentialsMatcher) {
        AllowAllCredentialsMatcher matcher = new AllowAllCredentialsMatcher();
        super.setCredentialsMatcher(matcher);
    }
}
