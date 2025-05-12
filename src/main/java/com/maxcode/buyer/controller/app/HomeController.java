package com.maxcode.buyer.controller.app;

import com.maxcode.buyer.entities.Account;
import com.maxcode.buyer.entities.VipLevel;
import com.maxcode.buyer.dao.AccountDao;
import com.maxcode.buyer.service.VipLevelService;
import com.maxcode.buyer.entities.dto.VipLevelDTO;
import com.maxcode.buyer.entities.dto.VipProgressDTO;
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
import java.util.List;

@RestController
@RequestMapping("/api/home")
public class HomeController {

    @Autowired
    private VipLevelService vipLevelService;
    
    /**
     * 获取所有VIP等级信息
     */
    @GetMapping("/vip/levels")
    public ResponseEntity<List<VipLevelDTO>> getAllVipLevels() {
        return new ResponseEntity<>(vipLevelService.getAllVipLevels(), HttpStatus.OK);
    }
    
    /**
     * 获取用户VIP等级进度
     */
    @GetMapping("/vip/progress/{uid}")
    public ResponseEntity<VipProgressDTO> getUserVipProgress(@PathVariable Long uid) {
        return new ResponseEntity<>(vipLevelService.getUserVipProgress(uid), HttpStatus.OK);
    }
} 