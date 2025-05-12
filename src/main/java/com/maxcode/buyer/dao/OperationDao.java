package com.maxcode.buyer.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Param;

import com.maxcode.buyer.entities.Operation;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface OperationDao extends BaseMapper<Operation> {
    
    @Select("SELECT * FROM operations WHERE id = #{id}")
    Operation findById(@Param("id") Long id);
    
    @Select("SELECT * FROM operations WHERE uid = #{uid}")
    IPage<Operation> findByUid(@Param("uid") Long uid, Page<Operation> page);
    
    @Select("SELECT * FROM operations WHERE type = #{type}")
    IPage<Operation> findByType(@Param("type") String type, Page<Operation> page);
    
    List<Operation> findByMultipleConditions(
        @Param("uid") Long uid,
        @Param("type") String type,
        @Param("minAmount") BigDecimal minAmount,
        @Param("maxAmount") BigDecimal maxAmount,
        @Param("startTime") LocalDateTime startTime,
        @Param("endTime") LocalDateTime endTime
    );
    
    int updateOperationSelective(Operation operation);
} 