package com.maxcode.buyer.entities;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("orders")
public class Order {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private Long uid;
    
    private String status;
    
    private BigDecimal amount;
    
    private BigDecimal rate;
    
    private BigDecimal profit;
    
    private Integer duration;
    
    private Long productId;
    
    private LocalDateTime createTime;
    
    private LocalDateTime updateTime;
} 