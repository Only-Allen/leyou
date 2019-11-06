package com.leyou.user.controller;

import com.leyou.user.pojo.User;
import com.leyou.user.service.UserService;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.validation.FieldError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.stream.Collectors;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/check/{data}/{type}")
    public ResponseEntity<Boolean> checkValue(
            @PathVariable("data") String data,
            @PathVariable("type") Integer type) {
        return ResponseEntity.ok(userService.checkValue(data, type));
    }

    @PostMapping("/code")
    public ResponseEntity<Void> sendMessage(String phone) {
        userService.sendMessage(phone);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@Valid UserInsertPO userInsertPO, BindingResult result) {
        if (result.hasFieldErrors()) {
            throw new RuntimeException(result.getFieldErrors().stream()
                    .map(FieldError::getDefaultMessage).collect(Collectors.joining(" | ")));
        }
        userService.register(userInsertPO.toPojo(), userInsertPO.getCode());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/query")
    public ResponseEntity<User> queryUserByUsernameAndPassword(
            @RequestParam("username") String username,
            @RequestParam("password") String password
    ) {
        return ResponseEntity.ok(userService.queryUserByUsernameAndPassword(username, password));
    }

    @Data
    private static class UserInsertPO {
        private static BeanCopier copier = BeanCopier.create(UserInsertPO.class, User.class, false);
        @Length(min = 4, max = 30, message = "用户名只能在4~30位之间")
        private String username;
        @Length(min = 4, max = 30, message = "密码只能在4~30位之间")
        private String password;
        @Pattern(regexp = "^1[35678]\\d{9}$", message = "手机号格式不正确")
        private String phone;
        @Size(min = 6, max = 6, message = "验证码格式错误")
        private String code;

        public User toPojo() {
            User user = new User();
            copier.copy(this, user, null);
            return user;
        }
    }
}
