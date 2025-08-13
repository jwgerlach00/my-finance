package com.gerlach.myfinance.parser;

import com.opencsv.exceptions.CsvValidationException;
import com.gerlach.myfinance.data.Statement;
import com.gerlach.myfinance.data.TransactionRecord;

import java.io.IOException;
import java.util.List;

public interface TransactionParser {
    List<TransactionRecord> parse(Statement statement) throws IOException, CsvValidationException;
}
