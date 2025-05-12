package com.maxcode.buyer.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Param;

import com.maxcode.buyer.entities.Order;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface OrderDao extends BaseMapper<Order> {
    
    @Select("SELECT * FROM orders WHERE id = #{id}")
    Order findById(@Param("id") Long id);
    
    @Select("SELECT * FROM orders WHERE uid = #{uid}")
    IPage<Order> findByUid(@Param("uid") Long uid, Page<Order> page);
    
    @Select("SELECT * FROM orders WHERE product_id = #{productId}")
    IPage<Order> findByProductId(@Param("productId") Long productId, Page<Order> page);
    
    @Select("SELECT * FROM orders WHERE status = #{status}")
    IPage<Order> findByStatus(@Param("status") String status, Page<Order> page);
    
    List<Order> findByMultipleConditions(
        @Param("uid") Long uid,
        @Param("productId") Long productId,
        @Param("status") String status,
        @Param("minAmount") BigDecimal minAmount,
        @Param("maxAmount") BigDecimal maxAmount,
        @Param("minProfit") BigDecimal minProfit,
        @Param("maxProfit") BigDecimal maxProfit,
        @Param("startTime") LocalDateTime startTime,
        @Param("endTime") LocalDateTime endTime
    );
    
    int updateOrderSelective(Order order);
} 