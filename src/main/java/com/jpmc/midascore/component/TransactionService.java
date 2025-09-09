package com.jpmc.midascore.component;

import com.jpmc.midascore.entity.TransactionRecord;
import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.foundation.Transaction;
import com.jpmc.midascore.repository.TransactionRecordRepository;
import com.jpmc.midascore.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TransactionService {
    
    private static final Logger logger = LoggerFactory.getLogger(TransactionService.class);
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private TransactionRecordRepository transactionRecordRepository;
    
    @Transactional
    public boolean processTransaction(Transaction transaction) {
        try {
            // Validate and get sender
            UserRecord sender = userRepository.findById(transaction.getSenderId());
            if (sender == null) {
                logger.warn("Invalid sender ID: {}", transaction.getSenderId());
                return false;
            }
            
            // Validate and get recipient
            UserRecord recipient = userRepository.findById(transaction.getRecipientId());
            if (recipient == null) {
                logger.warn("Invalid recipient ID: {}", transaction.getRecipientId());
                return false;
            }
            
            // Check if sender has sufficient balance
            if (sender.getBalance() < transaction.getAmount()) {
                logger.warn("Insufficient balance. Sender: {}, Balance: {}, Amount: {}", 
                    sender.getName(), sender.getBalance(), transaction.getAmount());
                return false;
            }
            
            // Process the transaction
            sender.setBalance(sender.getBalance() - transaction.getAmount());
            recipient.setBalance(recipient.getBalance() + transaction.getAmount());
            
            // Save updated balances
            userRepository.save(sender);
            userRepository.save(recipient);
            
            // Create and save transaction record
            TransactionRecord record = new TransactionRecord(sender, recipient, transaction.getAmount());
            transactionRecordRepository.save(record);
            
            logger.info("Transaction processed successfully: {} -> {} amount: {}", 
                sender.getName(), recipient.getName(), transaction.getAmount());
            
            return true;
            
        } catch (Exception e) {
            logger.error("Error processing transaction: {}", e.getMessage(), e);
            return false;
        }
    }
}
