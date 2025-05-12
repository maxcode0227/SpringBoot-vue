package com.maxcode.buyer.controller;

import com.maxcode.buyer.entities.VipLevel;
import com.maxcode.buyer.dao.VipLevelDao;
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
@RequestMapping("/api/vip-levels")
public class VipLevelController {

    @Autowired
    private VipLevelDao vipLevelDao;

    @Value("${com.boylegu.paginatio.max-per-page}")
    Integer maxPerPage;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getVipLevels(
            @RequestParam(value = "page", defaultValue = "1") Integer page) {
        
        Page<VipLevel> pageable = new Page<>(page, maxPerPage);
        Page<VipLevel> result = vipLevelDao.selectPage(pageable, new QueryWrapper<>());
        
        Map<String, Object> response = new HashMap<>();
        response.put("content", result.getRecords());
        response.put("totalElements", result.getTotal());
        response.put("totalPages", result.getPages());
        response.put("currentPage", page);
        
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getVipLevel(@PathVariable Long id) {
        VipLevel vipLevel = vipLevelDao.selectById(id);
        if (vipLevel == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(vipLevel, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> createVipLevel(@RequestBody VipLevel vipLevel) {
        // 检查level是否已存在
        QueryWrapper<VipLevel> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("level", vipLevel.getLevel());
        if (vipLevelDao.selectOne(queryWrapper) != null) {
            return new ResponseEntity<>("该VIP等级已存在", HttpStatus.BAD_REQUEST);
        }
        
        vipLevelDao.insert(vipLevel);
        return new ResponseEntity<>(vipLevel, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateVipLevel(@PathVariable Long id, @RequestBody VipLevel vipLevel) {
        VipLevel existingVipLevel = vipLevelDao.selectById(id);
        if (existingVipLevel == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // 检查level是否与其他记录冲突
        if (!existingVipLevel.getLevel().equals(vipLevel.getLevel())) {
            QueryWrapper<VipLevel> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("level", vipLevel.getLevel());
            if (vipLevelDao.selectOne(queryWrapper) != null) {
                return new ResponseEntity<>("该VIP等级已存在", HttpStatus.BAD_REQUEST);
            }
        }

        vipLevel.setId(id);
        vipLevelDao.updateById(vipLevel);
        return new ResponseEntity<>(vipLevel, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteVipLevel(@PathVariable Long id) {
        VipLevel vipLevel = vipLevelDao.selectById(id);
        if (vipLevel == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        vipLevelDao.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/check-eligibility")
    public ResponseEntity<?> checkVipEligibility(
            @RequestParam Double amount,
            @RequestParam Integer orders) {
        VipLevel eligibleVipLevel = vipLevelDao.findEligibleVipLevel(amount, orders);
        if (eligibleVipLevel == null) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "未找到符合条件的VIP等级");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(eligibleVipLevel, HttpStatus.OK);
    }
} 