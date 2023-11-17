package com.smallworld;

import com.smallworld.data.Transaction;

import java.util.*;
import java.util.stream.Collectors;

public class TransactionDataFetcher {

    private List<Transaction> transactions;

    public TransactionDataFetcher(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    /**
     * Returns the sum of the amounts of all transactions
     */
    public double getTotalTransactionAmount() {
        double totalAmount = 0.0;

        for (Transaction transaction : transactions) {
            totalAmount += transaction.getAmount();
        }

        return totalAmount;
    }

    /**
     * Returns the sum of the amounts of all transactions sent by the specified client
     */
    public double getTotalTransactionAmountSentBy(String senderFullName) {
        double totalAmountSentBy = 0.0;

        for (Transaction transaction : transactions) {
            // Check if the transaction is sent by the specified sender
            if (senderFullName.equals(transaction.getSenderFullName())) {
                totalAmountSentBy += transaction.getAmount();
            }
        }

        return totalAmountSentBy;
    }

    /**
     * Returns the highest transaction amount
     */
    public double getMaxTransactionAmount() {
        throw new UnsupportedOperationException();
    }

    /**
     * Counts the number of unique clients that sent or received a transaction
     */
    public long countUniqueClients() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns whether a client (sender or beneficiary) has at least one transaction with a compliance
     * issue that has not been solved
     */
    public boolean hasOpenComplianceIssues(String clientFullName) {
        for (Transaction transaction : transactions) {
            // Check if the client is the sender or beneficiary and if there is an open compliance issue
            if ((clientFullName.equals(transaction.getSenderFullName()) || clientFullName.equals(transaction.getBeneficiaryFullName()))
                    && transaction.getIssueSolved()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns all transactions indexed by beneficiary name
     */
    public Map<String, Transaction> getTransactionsByBeneficiaryName() {
        Map<String, Transaction> transactionsByBeneficiary = new HashMap<>();

        for (Transaction transaction : transactions) {
            String beneficiaryName = transaction.getBeneficiaryFullName();
            transactionsByBeneficiary.put(beneficiaryName, transaction);
        }

        return transactionsByBeneficiary;
    }

    /**
     * Returns the identifiers of all open compliance issues
     */
    public List<Integer> getUnsolvedIssueIds() {
        List<Integer> unsolvedIssueIds = new ArrayList<>();

        for (Transaction transaction : transactions) {
            if (transaction.getIssueSolved() == false) {
                unsolvedIssueIds.add(transaction.getIssueId());
            }
        }

        return unsolvedIssueIds;
    }

    /**
     * Returns a list of all solved issue messages
     */
    public List<String> getAllSolvedIssueMessages() {
        List<String> solvedIssueMessages = new ArrayList<>();

        for (Transaction transaction : transactions) {
            if (transaction.getIssueSolved()) {
                solvedIssueMessages.add(transaction.getIssueMessage());
            }
        }

        return solvedIssueMessages;
    }

    /**
     * Returns the 3 transactions with the highest amount sorted by amount descending
     */
    public List<Transaction> getTop3TransactionsByAmount() {
        List<Transaction> sortedTransactions = transactions.stream()
                .sorted(Comparator.comparingDouble(Transaction::getAmount).reversed())
                .collect(Collectors.toList());

        // Get the top 3 transactions or all transactions if there are fewer than 3
        return sortedTransactions.subList(0, Math.min(3, sortedTransactions.size()));
    }

    /**
     * Returns the senderFullName of the sender with the most total sent amount
     */
    public Optional<String> getTopSender() {
        Map<String, Double> totalSentAmountBySender = new HashMap<>();

        // Calculate total sent amount for each sender
        for (Transaction transaction : transactions) {
            String senderName = transaction.getSenderFullName();
            double amount = transaction.getAmount();
            totalSentAmountBySender.put(senderName, totalSentAmountBySender.getOrDefault(senderName, 0.0) + amount);
        }

        // Find the sender with the maximum total sent amount
        Optional<Map.Entry<String, Double>> topSenderEntry = totalSentAmountBySender.entrySet()
                .stream()
                .max(Map.Entry.comparingByValue());

        // Return the senderFullName if found
        return topSenderEntry.map(Map.Entry::getKey);
    }

}
