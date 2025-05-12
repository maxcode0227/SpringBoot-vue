package com.maxcode.buyer.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Param;

import com.maxcode.buyer.entities.AccountBalance;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface AccountBalanceDao extends BaseMapper<AccountBalance> {
    
    @Select("SELECT * FROM account_balance WHERE id = #{id}")
    AccountBalance findById(@Param("id") Long id);
    
    @Select("SELECT * FROM account_balance WHERE uid = #{uid}")
    AccountBalance findByUid(@Param("uid") Long uid);
    
    @Select("SELECT * FROM account_balance")
    IPage<AccountBalance> findAll(Page<AccountBalance> page);
    
    List<AccountBalance> findByMultipleConditions(
        @Param("uid") Long uid,
        @Param("minTotalAmount") BigDecimal minTotalAmount,
        @Param("maxTotalAmount") BigDecimal maxTotalAmount,
        @Param("minTotalProfit") BigDecimal minTotalProfit,
        @Param("maxTotalProfit") BigDecimal maxTotalProfit,
        @Param("startTime") LocalDateTime startTime,
        @Param("endTime") LocalDateTime endTime
    );
    
    int updateAccountBalanceSelective(AccountBalance accountBalance);
} 