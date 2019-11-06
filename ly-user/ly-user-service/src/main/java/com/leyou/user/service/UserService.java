package com.leyou.user.service;

import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.exception.LyException;
import com.leyou.common.utils.NumberUtils;
import com.leyou.user.mapper.UserMapper;
import com.leyou.user.pojo.User;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private AmqpTemplate amqpTemplate;
    @Autowired
    private StringRedisTemplate redisTemplate;
    private static final String USER_CODE_PREFIX = "user:code:";

    public Boolean checkValue(String data, Integer type) {
        User user = new User();
        switch (type) {
            case 1:
                user.setUsername(data);
                break;
            case 2:
                user.setPhone(data);
                break;
            default:
                throw new LyException(ExceptionEnum.INVALID_USER_DATA_TYPE);
        }
        int count = userMapper.selectCount(user);
        return count == 0;
    }

    public Void sendMessage(String phone) {
        String code = NumberUtils.generateCode(6);
        Map<String, String> msg = new HashMap<>();
        msg.put("phone", phone);
        msg.put("code", code);
        String key = USER_CODE_PREFIX + phone;
        redisTemplate.opsForValue().set(key, code, 5, TimeUnit.MINUTES);
        amqpTemplate.convertAndSend("ly.sms.exchange", "sms.verify.code", msg);
        return null;
    }

    public Void register(User user, String code) {
        String cacheCode = redisTemplate.opsForValue().get(USER_CODE_PREFIX + user.getPhone());
        if (!StringUtils.equals(cacheCode, code)) {
            throw new LyException(ExceptionEnum.INVALID_VALIDATION_CODE);
        }
        user.setCreated(new Date());
        user.setPassword(DigestUtils.md5Hex(user.getPassword()));
        int count = userMapper.insert(user);
        if (count < 1) {
            throw new LyException(ExceptionEnum.REGISTER_USER_ERROR);
        }
        return null;
    }

    public User queryUserByUsernameAndPassword(String username, String password) {
        User u = new User();
        u.setUsername(username);
        User user = userMapper.selectOne(u);
        if (user == null) {
            throw new LyException(ExceptionEnum.INVALID_USERNAME_OR_PASSWORD);
        }
        if (!StringUtils.equals(DigestUtils.md5Hex(password), user.getPassword())) {
            throw new LyException(ExceptionEnum.INVALID_USERNAME_OR_PASSWORD);
        }
        return user;
    }
}
