package com.maxcode.buyer.controller;

import com.maxcode.buyer.entities.VipLevel;
import com.maxcode.buyer.dao.VipLevelDao;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class VipLevelControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private VipLevelDao vipLevelDao;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testGetVipLevels() throws Exception {
        mockMvc.perform(get("/api/vip-levels")
                .param("page", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(greaterThan(0))))
                .andExpect(jsonPath("$.totalElements", greaterThan(0)))
                .andExpect(jsonPath("$.currentPage", is(1)));
    }

    @Test
    public void testGetVipLevel() throws Exception {
        // 使用已知的VIP等级ID（1）进行测试
        mockMvc.perform(get("/api/vip-levels/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.level", is(1)))
                .andExpect(jsonPath("$.thresholdAmount", is(1000.00)))
                .andExpect(jsonPath("$.thresholdOrders", is(10)));
    }

    @Test
    public void testCreateVipLevel() throws Exception {
        VipLevel newVipLevel = new VipLevel();
        newVipLevel.setLevel(5);
        newVipLevel.setThresholdAmount(new BigDecimal("200000.00"));
        newVipLevel.setThresholdOrders(500);
        newVipLevel.setRate(new BigDecimal("5.00"));
        newVipLevel.setImageUrl("https://example.com/vip/level5.png");

        mockMvc.perform(post("/api/vip-levels")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newVipLevel)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.level", is(5)))
                .andExpect(jsonPath("$.thresholdAmount", is(200000.00)))
                .andExpect(jsonPath("$.thresholdOrders", is(500)))
                .andExpect(jsonPath("$.rate", is(5.00)));
    }

    @Test
    public void testUpdateVipLevel() throws Exception {
        VipLevel updateVipLevel = new VipLevel();
        updateVipLevel.setThresholdAmount(new BigDecimal("1200.00"));
        updateVipLevel.setRate(new BigDecimal("0.60"));

        mockMvc.perform(put("/api/vip-levels/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateVipLevel)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.thresholdAmount", is(1200.00)))
                .andExpect(jsonPath("$.rate", is(0.60)));
    }

    @Test
    public void testDeleteVipLevel() throws Exception {
        // 创建一个新的VIP等级用于删除测试
        VipLevel vipLevel = new VipLevel();
        vipLevel.setLevel(6);
        vipLevel.setThresholdAmount(new BigDecimal("300000.00"));
        vipLevel.setThresholdOrders(1000);
        vipLevel.setRate(new BigDecimal("6.00"));
        vipLevelDao.insert(vipLevel);

        mockMvc.perform(delete("/api/vip-levels/" + vipLevel.getId()))
                .andExpect(status().isNoContent());

        // 验证删除后无法找到
        mockMvc.perform(get("/api/vip-levels/" + vipLevel.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCheckEligibility() throws Exception {
        mockMvc.perform(get("/api/vip-levels/check-eligibility")
                .param("amount", "6000.0")
                .param("orders", "35"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.level", is(2)));

        // 测试不符合条件的情况
        mockMvc.perform(get("/api/vip-levels/check-eligibility")
                .param("amount", "500.0")
                .param("orders", "5"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is("未找到符合条件的VIP等级")));
    }

    @Test
    public void testCreateDuplicateLevel() throws Exception {
        VipLevel newVipLevel = new VipLevel();
        newVipLevel.setLevel(1); // 使用已存在的等级
        newVipLevel.setThresholdAmount(new BigDecimal("2000.00"));
        newVipLevel.setThresholdOrders(20);
        newVipLevel.setRate(new BigDecimal("1.00"));

        mockMvc.perform(post("/api/vip-levels")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newVipLevel)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("该VIP等级已存在"));
    }
} 