package com.maxcode.buyer.service.impl;

import com.maxcode.buyer.service.VipLevelService;
import com.maxcode.buyer.dao.VipLevelDao;
import com.maxcode.buyer.dao.OrderDao;
import com.maxcode.buyer.entities.VipLevel;
import com.maxcode.buyer.entities.Order;
import com.maxcode.buyer.entities.dto.VipLevelDTO;
import com.maxcode.buyer.entities.dto.VipProgressDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.beans.BeanUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import java.util.List;
import java.util.stream.Collectors;
import java.math.BigDecimal;

@Service
public class VipLevelServiceImpl implements VipLevelService {

    @Autowired
    private VipLevelDao vipLevelDao;
    
    @Autowired
    private OrderDao orderDao;

    @Override
    public List<VipLevelDTO> getAllVipLevels() {
        QueryWrapper<VipLevel> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc("level");
        return vipLevelDao.selectList(queryWrapper)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public VipProgressDTO getUserVipProgress(Long uid) {
        VipProgressDTO progressDTO = new VipProgressDTO();
        
        // 获取用户订单总数和总金额
        QueryWrapper<Order> orderQuery = new QueryWrapper<>();
        orderQuery.eq("uid", uid)
                 .eq("status", "COMPLETED");
        
        List<Order> orders = orderDao.selectList(orderQuery);
        int orderCount = orders.size();
        BigDecimal totalAmount = orders.stream()
                .map(Order::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        // 获取用户当前VIP等级信息
        VipLevel currentLevel = vipLevelDao.findEligibleVipLevel(totalAmount.doubleValue(), orderCount);
        if (currentLevel == null) {
            currentLevel = vipLevelDao.findByLevel(1); // 默认VIP1
        }
        
        // 获取下一级VIP信息
        QueryWrapper<VipLevel> nextLevelQuery = new QueryWrapper<>();
        nextLevelQuery.gt("level", currentLevel.getLevel())
                     .orderByAsc("level")
                     .last("LIMIT 1");
        VipLevel nextLevel = vipLevelDao.selectOne(nextLevelQuery);
        
        // 设置基本信息
        progressDTO.setCurrentLevel(convertToDTO(currentLevel));
        progressDTO.setOrderCount(orderCount);
        progressDTO.setTotalAmount(totalAmount);
        
        if (nextLevel != null) {
            progressDTO.setNextLevel(convertToDTO(nextLevel));
            // 计算订单数量进度
            double orderProgress = Math.min(100.0, 
                (double)orderCount / nextLevel.getThresholdOrders() * 100);
            // 计算金额进度
            double amountProgress = Math.min(100.0, 
                totalAmount.doubleValue() / nextLevel.getThresholdAmount().doubleValue() * 100);
            
            progressDTO.setOrderProgress(orderProgress);
            progressDTO.setAmountProgress(amountProgress);
        } else {
            // 已是最高等级
            progressDTO.setOrderProgress(100.0);
            progressDTO.setAmountProgress(100.0);
        }
        
        return progressDTO;
    }
    
    /**
     * 将VipLevel实体转换为DTO
     */
    private VipLevelDTO convertToDTO(VipLevel vipLevel) {
        if (vipLevel == null) {
            return null;
        }
        VipLevelDTO dto = new VipLevelDTO();
        BeanUtils.copyProperties(vipLevel, dto);
        return dto;
    }
} 