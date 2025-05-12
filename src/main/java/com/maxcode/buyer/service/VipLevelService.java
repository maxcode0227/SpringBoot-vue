package com.maxcode.buyer.service;

import com.maxcode.buyer.entities.dto.VipLevelDTO;
import com.maxcode.buyer.entities.dto.VipProgressDTO;
import java.util.List;

public interface VipLevelService {
    
    /**
     * 获取所有VIP等级信息
     * @return VIP等级列表
     */
    List<VipLevelDTO> getAllVipLevels();
    
    /**
     * 获取用户的VIP等级进度信息
     * @param uid 用户ID
     * @return 用户VIP进度信息
     */
    VipProgressDTO getUserVipProgress(Long uid);
} 