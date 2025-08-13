package com.gerlach.myfinance.parser;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import com.gerlach.myfinance.data.Bank;
import com.gerlach.myfinance.data.Statement;
import com.gerlach.myfinance.data.TransactionRecord;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class WellsFargoParser implements TransactionParser {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("MM/dd/yyyy");

    @Override
    public List<TransactionRecord> parse(Statement statement) throws IOException, CsvValidationException {
        File file = new File(statement.getFilePath());

        List<TransactionRecord> transactions = new ArrayList<>();

        try (CSVReader reader = new CSVReader(new FileReader(file))) {
            String[] row;

            while ((row = reader.readNext()) != null) {
                if (row.length < 5 || row[0].isBlank()) continue;

                LocalDate transactionDate = LocalDate.parse(row[0].trim(), FORMATTER);
                BigDecimal amount = new BigDecimal(row[1].trim());
                String description = row[4].trim();

                TransactionRecord record = new TransactionRecord();
                record.setTransactionDate(transactionDate);
                record.setPostedDate(null); // not provided in Wells Fargo format
                record.setAmount(amount);
                record.setDescription(description);
                record.setProvidedCategory(null); // not provided
                record.setBank(Bank.WELLS_FARGO);
                record.setMultiplier(statement.getMultiplier());
                record.setAdjustedAmount(record.getAmount().multiply(record.getMultiplier()));

                transactions.add(record);
            }
        }

        return transactions;
    }
}
