package com.maxcode.buyer.entities.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class VipProgressDTO {
    private VipLevelDTO currentLevel;
    private VipLevelDTO nextLevel;
    private Integer orderCount;
    private BigDecimal totalAmount;
    private Double orderProgress;
    private Double amountProgress;
} 