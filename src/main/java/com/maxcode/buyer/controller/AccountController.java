package com.maxcode.buyer.controller;

import com.maxcode.buyer.entities.Account;
import com.maxcode.buyer.dao.AccountRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.DigestUtils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    @Autowired
    private AccountRepository accountRepository;

    @Value("${com.boylegu.paginatio.max-per-page}")
    Integer maxPerPage;

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAccounts(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "username", required = false) String username,
            @RequestParam(value = "vipLevel", required = false) Integer vipLevel) {

        Page<Account> pageable = new Page<>(page, maxPerPage);
        
        QueryWrapper<Account> queryWrapper = new QueryWrapper<>();
        if (username != null && !username.isEmpty()) {
            queryWrapper.like("username", username);
        }
        if (vipLevel != null) {
            queryWrapper.eq("vip_level", vipLevel);
        }
        
        Page<Account> result = accountRepository.selectPage(pageable, queryWrapper);
        
        Map<String, Object> response = new HashMap<>();
        response.put("content", result.getRecords());
        response.put("totalElements", result.getTotal());
        response.put("totalPages", result.getPages());
        response.put("currentPage", page);
        
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getAccount(@PathVariable Long id) {
        Account account = accountRepository.selectById(id);
        if (account == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(account, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> createAccount(@RequestBody Account account) {
        accountRepository.insert(account);
        return new ResponseEntity<>(account, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<?> updateAccount(@PathVariable Long id, @RequestBody Account account) {
        Account existingAccount = accountRepository.selectById(id);
        if (existingAccount == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        account.setId(id);
        accountRepository.updateById(account);
        return new ResponseEntity<>(account, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteAccount(@PathVariable Long id) {
        Account account = accountRepository.selectById(id);
        if (account == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        accountRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @RequestMapping(value = "/vip/{level}", method = RequestMethod.GET)
    public ResponseEntity<?> getAccountsByVipLevel(
            @PathVariable Integer level,
            @RequestParam(value = "page", defaultValue = "1") Integer page) {
        
        Page<Account> pageable = new Page<>(page, maxPerPage);
        return new ResponseEntity<>(
            accountRepository.findByVipLevel(level, pageable), 
            HttpStatus.OK
        );
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginRequest) {
        String username = loginRequest.get("username");
        String password = loginRequest.get("password");
        
        if (username == null || password == null) {
            return new ResponseEntity<>("用户名和密码不能为空", HttpStatus.BAD_REQUEST);
        }

        String encryptedPassword = DigestUtils.md5DigestAsHex(password.getBytes());
        
        Account account = accountRepository.findByUsernameAndPassword(username, encryptedPassword);
        
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