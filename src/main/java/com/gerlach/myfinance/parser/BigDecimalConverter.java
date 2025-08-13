package com.gerlach.myfinance.parser;

import com.opencsv.bean.AbstractBeanField;

import java.math.BigDecimal;

public class BigDecimalConverter extends AbstractBeanField<BigDecimal, String> {
    @Override
    protected BigDecimal convert(String value) {
        return new BigDecimal(value.replace(",", ""));
    }
}