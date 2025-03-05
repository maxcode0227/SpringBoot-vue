package com.maxcode.buyer.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Param;

import com.maxcode.buyer.entities.Persons;

import java.util.List;

@Mapper
public interface PersonsRepository extends BaseMapper<Persons> {

    @Select("SELECT DISTINCT sex FROM persons")
    List<String> findSex();

    @Select("SELECT * FROM persons")
    IPage<Persons> findAll(Page<Persons> page);

    @Select("SELECT * FROM persons WHERE sex = #{sexName} AND email LIKE CONCAT('%',#{emailName},'%')")
    IPage<Persons> findBySexAndEmailContains(@Param("sexName") String sexName, @Param("emailName") String emailName, Page<Persons> page);

    @Select("SELECT * FROM persons WHERE sex = #{sexName}")
    IPage<Persons> findBySex(@Param("sexName") String sexName, Page<Persons> page);

    @Select("SELECT * FROM persons WHERE id = #{id}")
    Persons findById(@Param("id") Long id);

}
