package com.maxcode.buyer.entities;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("operations")
public class Operation {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private Long uid;
    
    private BigDecimal amount;
    
    private String type;
    
    private LocalDateTime createTime;
    
    private LocalDateTime updateTime;
} 