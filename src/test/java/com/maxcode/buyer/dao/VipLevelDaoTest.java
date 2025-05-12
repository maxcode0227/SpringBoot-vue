package com.maxcode.buyer.dao;

import com.maxcode.buyer.entities.VipLevel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class VipLevelDaoTest {

    @Autowired
    private VipLevelDao vipLevelDao;

    @Test
    public void testFindById() {
        // 查找ID为1的VIP等级
        VipLevel vipLevel = vipLevelDao.findById(1L);
        assertNotNull(vipLevel);
        assertEquals(1, vipLevel.getLevel());
        assertEquals(new BigDecimal("1000.00"), vipLevel.getThresholdAmount());
        assertEquals(10, vipLevel.getThresholdOrders());
        assertEquals(new BigDecimal("0.50"), vipLevel.getRate());
    }

    @Test
    public void testFindByLevel() {
        // 查找等级为2的VIP
        VipLevel vipLevel = vipLevelDao.findByLevel(2);
        assertNotNull(vipLevel);
        assertEquals(2, vipLevel.getLevel());
        assertEquals(new BigDecimal("5000.00"), vipLevel.getThresholdAmount());
        assertEquals(30, vipLevel.getThresholdOrders());
        assertEquals(new BigDecimal("1.00"), vipLevel.getRate());
    }

    @Test
    public void testFindEligibleVipLevel() {
        // 测试符合条件的VIP等级查询
        VipLevel eligibleLevel = vipLevelDao.findEligibleVipLevel(6000.0, 35);
        assertNotNull(eligibleLevel);
        assertEquals(2, eligibleLevel.getLevel());

        // 测试超高条件
        VipLevel highestLevel = vipLevelDao.findEligibleVipLevel(200000.0, 500);
        assertNotNull(highestLevel);
        assertEquals(4, highestLevel.getLevel());

        // 测试不符合任何等级的条件
        VipLevel noLevel = vipLevelDao.findEligibleVipLevel(500.0, 5);
        assertNull(noLevel);
    }

    @Test
    public void testFindByMultipleConditions() {
        // 测试多条件查询
        List<VipLevel> levels = vipLevelDao.findByMultipleConditions(
            null,
            new BigDecimal("1000.00"),
            new BigDecimal("50000.00"),
            10,
            100,
            new BigDecimal("0.5"),
            new BigDecimal("2.0"),
            null,
            null
        );

        assertNotNull(levels);
        assertTrue(levels.size() >= 2);
        // 验证结果是否按level升序排序
        for (int i = 1; i < levels.size(); i++) {
            assertTrue(levels.get(i).getLevel() > levels.get(i-1).getLevel());
        }
    }

    @Test
    public void testUpdateVipLevelSelective() {
        // 创建测试数据
        VipLevel vipLevel = vipLevelDao.findById(1L);
        assertNotNull(vipLevel);

        // 更新部分字段
        vipLevel.setThresholdAmount(new BigDecimal("1500.00"));
        vipLevel.setRate(new BigDecimal("0.75"));

        int result = vipLevelDao.updateVipLevelSelective(vipLevel);
        assertEquals(1, result);

        // 验证更新结果
        VipLevel updatedLevel = vipLevelDao.findById(1L);
        assertNotNull(updatedLevel);
        assertEquals(new BigDecimal("1500.00"), updatedLevel.getThresholdAmount());
        assertEquals(new BigDecimal("0.75"), updatedLevel.getRate());
        // 确保其他字段未变
        assertEquals(1, updatedLevel.getLevel());
        assertEquals(10, updatedLevel.getThresholdOrders());
    }
} 