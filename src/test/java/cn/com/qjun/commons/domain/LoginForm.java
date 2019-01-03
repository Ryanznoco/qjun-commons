package cn.com.qjun.commons.domain;

import lombok.Data;

@Data
public class LoginForm {
    private String username;
    private String password;
    private String verifyCode;
}