package com.gerlach.myfinance.processor;

import com.gerlach.myfinance.data.TransactionRecord;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class CapitalOneProcessor extends BankProcessor {
    static final String PAYMENT_PROVIDED_CATEGORY = "payment/credit";

    @Override
    public List<TransactionRecord> removeCreditCardPayments(List<TransactionRecord> records) {
        return records.stream()
                .filter(record -> {
                    String providedCategory = record.getProvidedCategory().toLowerCase().trim();
                    if (providedCategory.equals(PAYMENT_PROVIDED_CATEGORY)) {
                        log.info("Credit card payment found with provided category: {}", providedCategory);
                    }
                    return !providedCategory.equals(PAYMENT_PROVIDED_CATEGORY);
                })
                .collect(Collectors.toList());
    }
}
