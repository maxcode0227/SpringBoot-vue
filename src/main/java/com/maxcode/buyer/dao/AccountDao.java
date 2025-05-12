package com.maxcode.buyer.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Param;

import com.maxcode.buyer.entities.Account;

import java.util.List;

@Mapper
public interface AccountDao extends BaseMapper<Account> {
    
    @Select("SELECT * FROM accounts WHERE id = #{id}")
    Account findById(@Param("id") Long id);
    
    @Select("SELECT * FROM accounts")
    IPage<Account> findAll(Page<Account> page);
    
    @Select("SELECT * FROM accounts WHERE username LIKE CONCAT('%',#{username},'%')")
    IPage<Account> findByUsernameLike(@Param("username") String username, Page<Account> page);
    
    @Select("SELECT * FROM accounts WHERE vip_level = #{vipLevel}")
    IPage<Account> findByVipLevel(@Param("vipLevel") Integer vipLevel, Page<Account> page);
    
    @Select("SELECT * FROM accounts WHERE username = #{username} AND login_password = #{password}")
    Account findByUsernameAndPassword(@Param("username") String username, @Param("password") String password);
} 