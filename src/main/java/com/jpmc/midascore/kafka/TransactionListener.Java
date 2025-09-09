package com.jpmc.midascore.kafka;

import com.jpmc.midascore.component.TransactionService;
import com.jpmc.midascore.foundation.Transaction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TransactionListener {

    @Autowired
    private TransactionService transactionService;

    @KafkaListener(topics = "${general.kafka-topic}")
    public void handleTransaction(Transaction transaction) {
        log.info("Received transaction: {}", transaction);
        
        // Process the transaction through the service (validates and saves to database)
        boolean success = transactionService.processTransaction(transaction);
        
        // This will help you see the amounts clearly in the console
        System.out.println("Transaction amount: " + transaction.getAmount() + ", Success: " + success);
        
        if (success) {
            log.info("Transaction processed and saved to database");
        } else {
            log.warn("Transaction rejected - invalid user or insufficient balance");
        }
        
        // Set your debugger breakpoint HERE to inspect the transaction object
        // You can examine transaction.getAmount() to record the values
    }
}


