package com.gerlach.myfinance.data;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class TransactionRecord {
    private LocalDate transactionDate;

    private LocalDate postedDate;

    private BigDecimal amount;

    private String description;

    private String providedCategory;

    private Bank bank;

    private ExpenseCategory category;

    private BigDecimal multiplier;

    private BigDecimal adjustedAmount;
}