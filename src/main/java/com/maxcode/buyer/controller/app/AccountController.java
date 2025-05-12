package com.maxcode.buyer.controller.app;

import com.maxcode.buyer.entities.Account;
import com.maxcode.buyer.service.AccountService;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/account")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginRequest) {
        String username = loginRequest.get("username");
        String password = loginRequest.get("password");
        
        if (username == null || password == null) {
            return new ResponseEntity<>("用户名和密码不能为空", HttpStatus.BAD_REQUEST);
        }

        Account account = accountService.login(username, password);
        
        Map<String, Object> response = new HashMap<>();
        if (account != null) {
            response.put("status", "success");
            response.put("message", "登录成功");
            response.put("accountId", account.getId());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            response.put("status", "error"); 
            response.put("message", "用户名或密码错误");
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }
    }
} 