package com.maxcode.buyer.entities;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("vip_levels")
public class VipLevel {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private Integer level;
    
    @TableField("threshold_amount")
    private BigDecimal thresholdAmount;
    
    @TableField("threshold_orders")
    private Integer thresholdOrders;
    
    private BigDecimal rate;
    
    @TableField("image_url")
    private String imageUrl;
    
    @TableField("create_time")
    private LocalDateTime createTime;
    
    @TableField("update_time")
    private LocalDateTime updateTime;
} 