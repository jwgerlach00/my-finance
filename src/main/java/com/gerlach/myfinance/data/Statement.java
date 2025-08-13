package com.gerlach.myfinance.data;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class Statement {
    private String filePath;
    private Bank bank;
    private BigDecimal multiplier = BigDecimal.ONE;
}
