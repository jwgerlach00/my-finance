package com.gerlach.myfinance.parser;

import com.opencsv.bean.AbstractBeanField;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LocalDateConverter extends AbstractBeanField<LocalDate, String> {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("MM/dd/yyyy");

    @Override
    protected LocalDate convert(String value) {
        return LocalDate.parse(value, FORMATTER);
    }
}