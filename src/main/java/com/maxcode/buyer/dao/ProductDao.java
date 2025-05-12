package com.maxcode.buyer.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Param;

import com.maxcode.buyer.entities.Product;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface ProductDao extends BaseMapper<Product> {
    
    @Select("SELECT * FROM products WHERE id = #{id}")
    Product findById(@Param("id") Long id);
    
    @Select("SELECT * FROM products")
    IPage<Product> findAll(Page<Product> page);
    
    @Select("SELECT * FROM products WHERE name LIKE CONCAT('%',#{name},'%')")
    IPage<Product> findByNameLike(@Param("name") String name, Page<Product> page);
    
    List<Product> findByMultipleConditions(
        @Param("name") String name,
        @Param("minAmount") BigDecimal minAmount,
        @Param("maxAmount") BigDecimal maxAmount,
        @Param("minDuration") Integer minDuration,
        @Param("maxDuration") Integer maxDuration,
        @Param("startTime") LocalDateTime startTime,
        @Param("endTime") LocalDateTime endTime
    );
    
    int updateProductSelective(Product product);
} 