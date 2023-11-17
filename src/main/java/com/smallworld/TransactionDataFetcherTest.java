package com.smallworld;

import com.fasterxml.jackson.databind.JsonNode;
import com.smallworld.data.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.nio.file.Files;
import java.nio.file.Path;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.junit.jupiter.api.Assertions.*;

public class TransactionDataFetcherTest {

    private TransactionDataFetcher dataFetcher;
    private List<Transaction> transactions = new ArrayList<>();
    @BeforeEach
    public void setUp() {
        // Initialize test data
        String filePath = "transactions.json";

        // Read JSON array from file into List<Transaction>
        try {
            // Create ObjectMapper instance
            ObjectMapper objectMapper = new ObjectMapper();

            JsonNode jsonArrayNode = objectMapper.readTree(new File(filePath));
            for (JsonNode jsonObjectNode : jsonArrayNode) {
                Transaction transaction = objectMapper.treeToValue(jsonObjectNode, Transaction.class);
                transactions.add(transaction);

                // Now you can work with each Transaction object
//                System.out.println("Transaction Data:");
//                System.out.println(transaction.getBeneficiaryFullName());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        dataFetcher = new TransactionDataFetcher(transactions);
    }

    @Test
    public void testGetTotalTransactionAmount() {
        double totalAmount = dataFetcher.getTotalTransactionAmount();
       System.out.print("Sum of Total Amount: "+totalAmount);
    }

    @Test
    public void testGetTotalTransactionAmountSentBy() {
        double totalAmount = dataFetcher.getTotalTransactionAmountSentBy(transactions.get(0).getSenderFullName());
        System.out.println("senderFullName: "+transactions.get(0).getSenderFullName()+" Sum of Total Amount: "+totalAmount);
    }

    // Add similar tests for other methods...

    @Test
    public void testHasOpenComplianceIssues() {
        boolean hasOpenIssues = dataFetcher.hasOpenComplianceIssues(transactions.get(0).getSenderFullName());
        System.out.print("Client Name: "+transactions.get(0).getSenderFullName()+ " Issue not been resolved: "+hasOpenIssues);
    }

    @Test
    public void testGetTransactionsByBeneficiaryName() {
        Map<String, Transaction> transactionsByBeneficiary = dataFetcher.getTransactionsByBeneficiaryName();

        for (Map.Entry<String, Transaction> entry : transactionsByBeneficiary.entrySet()) {
            String beneficiaryName = entry.getKey();
            Transaction transaction = entry.getValue();
            System.out.println("Beneficiary: " + beneficiaryName);
        }
    }

    @Test
    public void testGetUnsolvedIssueIds() {
        // Call the method to get the set of unsolved issue identifiers
        List<Integer> unsolvedIssueIds = dataFetcher.getUnsolvedIssueIds();
        for(int i = 0; i < unsolvedIssueIds.size(); i++) {
            System.out.println("Unresolved Issuses Id: "+unsolvedIssueIds.get(i));
        }

    }

    @Test
    public void testGetAllSolvedIssueMessages() {
        List<String> solvedIssueMessages = dataFetcher.getAllSolvedIssueMessages();
        for(int i = 0; i < solvedIssueMessages.size(); i++){
            System.out.println("Solved Message: "+solvedIssueMessages.get(i));
        }

    }

    @Test
    public void testGetTop3TransactionsByAmount() {
        List<Transaction> top3Transactions = dataFetcher.getTop3TransactionsByAmount();
        for(int i = 0; i < top3Transactions.size(); i++) {
            System.out.println(top3Transactions.get(i).getAmount());
        }
    }

    @Test
    public void testGetTopSender() {
        Optional<String> topSender = dataFetcher.getTopSender();
        System.out.println("Top Sender: " + topSender.orElse("No top sender found"));
    }
}