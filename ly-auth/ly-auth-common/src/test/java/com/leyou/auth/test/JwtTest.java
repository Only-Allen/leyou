package com.leyou.auth.test;

import com.leyou.auth.entity.UserInfo;
import com.leyou.auth.utils.JwtUtils;
import com.leyou.auth.utils.RsaUtils;
import org.junit.Before;
import org.junit.Test;

import java.security.PrivateKey;
import java.security.PublicKey;

public class JwtTest {

    private static final String PUBLIC_KEY_PATH = "D:\\IDEA\\leyou\\ras_tmp\\rsa.pub";
    private static final String PRIVATE_KEY_PATH = "D:\\IDEA\\leyou\\ras_tmp\\rsa.pri";

    private PublicKey publicKey;
    private PrivateKey privateKey;

    @Test
    public void testRsa() throws Exception {
        RsaUtils.generateKey(PUBLIC_KEY_PATH, PRIVATE_KEY_PATH, "123");
    }

    @Before
    public void testGetRsa() throws Exception {
        publicKey = RsaUtils.getPublicKey(PUBLIC_KEY_PATH);
        privateKey = RsaUtils.getPrivateKey(PRIVATE_KEY_PATH);
    }

    @Test
    public void testGenerateToken() throws Exception {
        String token = JwtUtils.generateToken(new UserInfo(29L, "xuange"), privateKey, 5);
        System.out.println("token = " + token);
    }

    @Test
    public void testParseToken() throws Exception {
        String token = "eyJhbGciOiJSUzI1NiJ9.eyJpZCI6MjksInVzZXJuYW1lIjoieHVhbmdlIiwiZXhwIjoxNTczNDYwNDM0fQ.XvQuAK6xw3K7mfym5laBFGJpkoZSrtKvxMUzKt0UKDbT2gF54KVjhq_kMsHXOP9g65NSyCkVeG9H1nchStnMs9nIzmbCese3SqbzbdS2NPltibYv5yWUeYpIU2jF6TNePFjWpuqBimKLKLpEGlffuoi_Akywm-joJlNC1zekzBs";
        UserInfo userInfo = JwtUtils.getInfoFromToken(token, publicKey);
        System.out.println("user: id = " + userInfo.getId() + ", name = " + userInfo.getUsername());
    }
}
