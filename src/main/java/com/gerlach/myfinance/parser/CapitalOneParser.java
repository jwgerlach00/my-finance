package com.gerlach.myfinance.parser;

import com.opencsv.bean.CsvToBeanBuilder;
import com.gerlach.myfinance.data.Bank;
import com.gerlach.myfinance.data.Statement;
import com.gerlach.myfinance.data.TransactionRecord;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class CapitalOneParser implements TransactionParser {

    @Override
    public List<TransactionRecord> parse(Statement statement) throws IOException {
        File file = new File(statement.getFilePath());

        List<CapitalOneCsvBean> beans = new CsvToBeanBuilder<CapitalOneCsvBean>(new FileReader(file))
                .withType(CapitalOneCsvBean.class)
                .withIgnoreLeadingWhiteSpace(true)
                .withIgnoreEmptyLine(true)
                .build()
                .parse();

        return beans.stream().map(bean -> {
            TransactionRecord record = new TransactionRecord();

            record.setTransactionDate(bean.getTransactionDate());
            record.setPostedDate(bean.getPostedDate());
            record.setDescription(bean.getDescription());
            record.setProvidedCategory(bean.getCategory());

            BigDecimal amount = null;
            if (bean.getDebit() != null && !bean.getDebit().isBlank()) {
                // Debit is negative
                amount = new BigDecimal(bean.getDebit()).negate();
            } else if (bean.getCredit() != null && !bean.getCredit().isBlank()) {
                // Credit is positive
                amount = new BigDecimal(bean.getCredit());
            }
            record.setAmount(amount);

            record.setBank(Bank.CAPITAL_ONE);
            record.setMultiplier(statement.getMultiplier());
            record.setAdjustedAmount(record.getAmount().multiply(record.getMultiplier()));

            return record;
        }).collect(Collectors.toList());
    }
}
