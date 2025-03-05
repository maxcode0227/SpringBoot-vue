package com.maxcode.buyer.entities;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("accounts")
public class Account {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private String username;
    
    private String avatarUrl;
    
    private String gender;
    
    private String withdrawPassword;
    
    private String loginPassword;
    
    private Integer vipLevel;
    
    private String createTime;
    
    private String updateTime;
} 