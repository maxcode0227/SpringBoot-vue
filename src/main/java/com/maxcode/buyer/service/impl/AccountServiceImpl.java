package com.maxcode.buyer.service.impl;

import com.maxcode.buyer.service.AccountService;
import com.maxcode.buyer.entities.Account;
import com.maxcode.buyer.dao.AccountDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

@Service
public class AccountServiceImpl implements AccountService {
    
    @Autowired
    private AccountDao accountDao;

    @Override
    public Account login(String username, String password) {
        if (username == null || password == null) {
            return null;
        }
        
        // MD5加密密码
        String encryptedPassword = DigestUtils.md5DigestAsHex(password.getBytes());
        
        // 查询用户
        return accountDao.findByUsernameAndPassword(username, encryptedPassword);
    }
} 