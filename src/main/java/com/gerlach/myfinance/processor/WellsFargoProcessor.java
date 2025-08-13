package com.gerlach.myfinance.processor;

import com.gerlach.myfinance.data.TransactionRecord;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
public class WellsFargoProcessor extends BankProcessor {
    static final Set<String> CREDIT_CARD_PAYMENTS = Set.of("automatic payment - thank you", "online payment thank you");

    @Override
    public List<TransactionRecord> removeCreditCardPayments(List<TransactionRecord> records) {
        return records.stream()
                .filter(record -> {
                    String desc = record.getDescription().toLowerCase().trim();
                    if (CREDIT_CARD_PAYMENTS.contains(desc)) {
                        log.info("Credit card payment found with description: {}", desc);
                    }
                    return !CREDIT_CARD_PAYMENTS.contains(desc);
                })
                .collect(Collectors.toList());
    }
}
