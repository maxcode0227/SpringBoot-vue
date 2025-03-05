package com.maxcode.buyer.entities;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("persons")
public class Persons {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String username;
    private String email;
    private String phone;
    private String sex;
    private String zone;
    private String createDatetime;
    // 其他字段根据需要添加
}