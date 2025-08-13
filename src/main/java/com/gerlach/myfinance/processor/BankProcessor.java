package com.gerlach.myfinance.processor;

import com.gerlach.myfinance.data.TransactionRecord;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

public abstract class BankProcessor {

    public abstract List<TransactionRecord> removeCreditCardPayments(List<TransactionRecord> transactions);

    public List<List<TransactionRecord>> findDuplicateCharges(List<TransactionRecord> transactions, int maxDayDiff) {
        List<List<TransactionRecord>> dupCharges = new ArrayList<>();

        // Sort transactions by date
        transactions.sort(Comparator.comparing(TransactionRecord::getTransactionDate));

        // Group by description and amount
        Map<String, List<TransactionRecord>> grouped = transactions.stream()
                .collect(Collectors.groupingBy(tx -> tx.getDescription().toLowerCase() + "|" + tx.getAmount()));

        for (List<TransactionRecord> group : grouped.values()) {
            if (group.size() < 2) continue;

            // Check dates within 1 or 2 days
            for (int i = 0; i < group.size() - 1; i++) {
                for (int j = i + 1; j < group.size(); j++) {
                    long daysBetween = ChronoUnit.DAYS.between(group.get(i).getTransactionDate(), group.get(j).getTransactionDate());
                    if (daysBetween <= maxDayDiff) {
                        dupCharges.add(Arrays.asList(group.get(i), group.get(j)));
                    }
                }
            }
        }

        return dupCharges;
    }

    public List<List<TransactionRecord>> findRefundPairs(List<TransactionRecord> transactions, int maxDayDiff) {
        List<List<TransactionRecord>> refundPairs = new ArrayList<>();

        // Sort by date ascending
        transactions.sort(Comparator.comparing(TransactionRecord::getTransactionDate));

        for (int i = 0; i < transactions.size() - 1; i++) {
            TransactionRecord t1 = transactions.get(i);
            String desc1 = t1.getDescription().toLowerCase();

            for (int j = i + 1; j < transactions.size(); j++) {
                TransactionRecord t2 = transactions.get(j);
                String desc2 = t2.getDescription().toLowerCase();

                long daysBetween = ChronoUnit.DAYS.between(t1.getTransactionDate(), t2.getTransactionDate());
                if (daysBetween > maxDayDiff) {
                    // If dates too far apart, no point continuing inner loop
                    break;
                }

                // Check if descriptions roughly match
                if (desc1.contains(desc2) || desc2.contains(desc1)) {
                    BigDecimal sum = t1.getAmount().add(t2.getAmount());

                    // Check if amounts negate each other within small tolerance, e.g. 1 cent
                    if (sum.abs().compareTo(new BigDecimal("0.01")) <= 0) {
                        // One is charge, other is refund
                        if ((t1.getAmount().compareTo(BigDecimal.ZERO) < 0 && t2.getAmount().compareTo(BigDecimal.ZERO) > 0) ||
                                (t1.getAmount().compareTo(BigDecimal.ZERO) > 0 && t2.getAmount().compareTo(BigDecimal.ZERO) < 0)) {
                            refundPairs.add(List.of(t1, t2));
                        }
                    }
                }
            }
        }

        return refundPairs;
    }

    public List<TransactionRecord> filterTransactionsByAmount(List<TransactionRecord> transactions, BigDecimal threshold, int comparisonType) {
        return transactions.stream()
                .filter(tx -> comparisonType == -1 ? tx.getAmount().compareTo(threshold) < 0 : tx.getAmount().compareTo(threshold) > 0)
                .collect(Collectors.toList());
    }
}
