package com.maxcode.buyer.controller;

import com.maxcode.buyer.entities.AccountBalance;
import com.maxcode.buyer.dao.AccountBalanceDao;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/account-balances")
public class AccountBalanceController {

    @Autowired
    private AccountBalanceDao accountBalanceDao;

    @Value("${com.boylegu.paginatio.max-per-page}")
    Integer maxPerPage;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAccountBalances(
            @RequestParam(value = "page", defaultValue = "1") Integer page) {
        
        Page<AccountBalance> pageable = new Page<>(page, maxPerPage);
        Page<AccountBalance> result = accountBalanceDao.selectPage(pageable, new QueryWrapper<>());
        
        Map<String, Object> response = new HashMap<>();
        response.put("content", result.getRecords());
        response.put("totalElements", result.getTotal());
        response.put("totalPages", result.getPages());
        response.put("currentPage", page);
        
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAccountBalance(@PathVariable Long id) {
        AccountBalance accountBalance = accountBalanceDao.selectById(id);
        if (accountBalance == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(accountBalance, HttpStatus.OK);
    }

    @GetMapping("/user/{uid}")
    public ResponseEntity<?> getAccountBalanceByUid(@PathVariable Long uid) {
        AccountBalance accountBalance = accountBalanceDao.findByUid(uid);
        if (accountBalance == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(accountBalance, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> createAccountBalance(@RequestBody AccountBalance accountBalance) {
        // 检查uid是否已存在
        QueryWrapper<AccountBalance> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("uid", accountBalance.getUid());
        if (accountBalanceDao.selectOne(queryWrapper) != null) {
            return new ResponseEntity<>("该用户账户余额记录已存在", HttpStatus.BAD_REQUEST);
        }
        
        accountBalanceDao.insert(accountBalance);
        return new ResponseEntity<>(accountBalance, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateAccountBalance(@PathVariable Long id, @RequestBody AccountBalance accountBalance) {
        AccountBalance existingBalance = accountBalanceDao.selectById(id);
        if (existingBalance == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        accountBalance.setId(id);
        accountBalanceDao.updateById(accountBalance);
        return new ResponseEntity<>(accountBalance, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAccountBalance(@PathVariable Long id) {
        AccountBalance accountBalance = accountBalanceDao.selectById(id);
        if (accountBalance == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        accountBalanceDao.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
} 