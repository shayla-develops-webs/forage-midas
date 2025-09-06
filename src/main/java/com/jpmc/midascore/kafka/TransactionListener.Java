package com.jpmc.midascore.kafka;

import com.jpmc.midascore.foundation.Transaction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TransactionListener {

    @KafkaListener(topics = "${general.kafka-topic}")
    public void handleTransaction(Transaction transaction) {
        log.info("Received transaction: {}", transaction);
        
        // This will help you see the amounts clearly in the console
        System.out.println("Transaction amount: " + transaction.getAmount());
        
        // Set your debugger breakpoint HERE to inspect the transaction object
        // You can examine transaction.getAmount() to record the values
    }
}


