package com.leyou.auth.service.impl;

import com.leyou.auth.client.UserClient;
import com.leyou.auth.config.JwtProperties;
import com.leyou.auth.entity.UserInfo;
import com.leyou.auth.service.AuthService;
import com.leyou.auth.utils.JwtUtils;
import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.exception.LyException;
import com.leyou.user.pojo.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@EnableConfigurationProperties(JwtProperties.class)
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserClient userClient;
    @Autowired
    private JwtProperties jwtProperties;

    @Override
    public String login(String username, String password) {
        try {
            User user = userClient.queryUserByUsernameAndPassword(username, password);
            if (user == null) {
                throw new LyException(ExceptionEnum.INVALID_USERNAME_OR_PASSWORD);
            }
            return JwtUtils.generateToken(new UserInfo(user.getId(), user.getUsername()), jwtProperties.getPrivateKey(), jwtProperties.getExpire());
        } catch (Exception e) {
            log.error("[授权中心] 用户名或密码错误，用户名称:{}", username, e);
            throw new LyException(ExceptionEnum.INVALID_USERNAME_OR_PASSWORD);
        }
    }
}
