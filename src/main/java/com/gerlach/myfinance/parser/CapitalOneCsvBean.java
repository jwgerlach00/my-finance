package com.gerlach.myfinance.parser;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvDate;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CapitalOneCsvBean {

    @CsvBindByName(column = "Transaction Date")
    @CsvDate("yyyy-MM-dd")
    private LocalDate transactionDate;

    @CsvBindByName(column = "Posted Date")
    @CsvDate("yyyy-MM-dd")
    private LocalDate postedDate;

    @CsvBindByName(column = "Description")
    private String description;

    @CsvBindByName(column = "Category")
    private String category;

    @CsvBindByName(column = "Debit")
    private String debit;

    @CsvBindByName(column = "Credit")
    private String credit;
}