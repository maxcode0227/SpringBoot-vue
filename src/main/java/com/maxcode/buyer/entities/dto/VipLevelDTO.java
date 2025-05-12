package com.maxcode.buyer.entities.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class VipLevelDTO {
    private Integer level;
    private BigDecimal thresholdAmount;
    private Integer thresholdOrders;
    private BigDecimal rate;
    private String imageUrl;
} 