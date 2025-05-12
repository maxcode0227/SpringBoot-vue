package com.maxcode.buyer.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Param;

import com.maxcode.buyer.entities.VipLevel;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface VipLevelDao extends BaseMapper<VipLevel> {
    
    @Select("SELECT * FROM vip_levels WHERE id = #{id}")
    VipLevel findById(@Param("id") Long id);
    
    @Select("SELECT * FROM vip_levels")
    IPage<VipLevel> findAll(Page<VipLevel> page);
    
    @Select("SELECT * FROM vip_levels WHERE level = #{level}")
    VipLevel findByLevel(@Param("level") Integer level);
    
    List<VipLevel> findByMultipleConditions(
        @Param("level") Integer level,
        @Param("minThresholdAmount") BigDecimal minThresholdAmount,
        @Param("maxThresholdAmount") BigDecimal maxThresholdAmount,
        @Param("minThresholdOrders") Integer minThresholdOrders,
        @Param("maxThresholdOrders") Integer maxThresholdOrders,
        @Param("minRate") BigDecimal minRate,
        @Param("maxRate") BigDecimal maxRate,
        @Param("startTime") LocalDateTime startTime,
        @Param("endTime") LocalDateTime endTime
    );
    
    int updateVipLevelSelective(VipLevel vipLevel);
    
    @Select("SELECT * FROM vip_levels WHERE threshold_amount <= #{amount} AND threshold_orders <= #{orders} ORDER BY level DESC LIMIT 1")
    VipLevel findEligibleVipLevel(@Param("amount") Double amount, @Param("orders") Integer orders);
} 