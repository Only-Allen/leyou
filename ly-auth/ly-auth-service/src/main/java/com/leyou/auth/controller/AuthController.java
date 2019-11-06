package com.leyou.auth.controller;

import com.leyou.auth.config.JwtProperties;
import com.leyou.auth.entity.UserInfo;
import com.leyou.auth.service.AuthService;
import com.leyou.auth.utils.JwtUtils;
import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.exception.LyException;
import com.leyou.common.utils.CookieUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@EnableConfigurationProperties(JwtProperties.class)
public class AuthController {

    @Autowired
    private AuthService authService;
    @Autowired
    private JwtProperties jwtProperties;

    @PostMapping("/login")
    public ResponseEntity<Void> login(
            HttpServletRequest request, HttpServletResponse response,
            @RequestParam("username") String username,
            @RequestParam("password") String password
    ) {
        String token = authService.login(username, password);
        //写入cookie
        CookieUtils.setCookie(request, response, jwtProperties.getCookieName(), token, jwtProperties.getExpire(), false);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/verify")
    public ResponseEntity<UserInfo> verify(
            HttpServletRequest request, HttpServletResponse response,
            @CookieValue("LY_TOKEN") String token) {
        if (StringUtils.isBlank(token)) {
            //没有token，表示未登录
            throw new LyException(ExceptionEnum.UNAUTHORIZED);
        }
        try {
            UserInfo info = JwtUtils.getInfoFromToken(token, jwtProperties.getPublicKey());
            String newToken = JwtUtils.generateToken(info, jwtProperties.getPrivateKey(), jwtProperties.getExpire());
            CookieUtils.setCookie(request, response, jwtProperties.getCookieName(), newToken, jwtProperties.getExpire(), false);
            return ResponseEntity.ok(info);
        } catch (Exception e) {
            //token过期或者无效
            throw new LyException(ExceptionEnum.UNAUTHORIZED);
        }
    }
}
