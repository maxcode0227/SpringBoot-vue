package com.maxcode.buyer.entities;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("products")
public class Product {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private String name;
    
    private BigDecimal amount;
    
    private String imageUrl;
    
    private Integer duration;
    
    private LocalDateTime createTime;
    
    private LocalDateTime updateTime;
} 