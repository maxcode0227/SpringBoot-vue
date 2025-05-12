package com.maxcode.buyer.service;

import com.maxcode.buyer.entities.Account;
import java.util.Map;

public interface AccountService {
    /**
     * 用户登录
     * @param username 用户名
     * @param password 密码
     * @return Account 账户信息
     */
    Account login(String username, String password);
} 