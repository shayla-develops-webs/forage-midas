package com.jpmc.midascore.service;

import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.repository.UserRepository;
import org.springframework.stereotype.Component;

import java.util.stream.StreamSupport;

@Component
public class TransactionConsumer {

    private final UserRepository userRepository;

    public TransactionConsumer(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Get user balance by name.
     * @param userName the name of the user (case-insensitive)
     * @return user's balance
     */
    public float getUserBalance(String userName) {
        return StreamSupport.stream(userRepository.findAll().spliterator(), false)
                .filter(u -> u.getName().equalsIgnoreCase(userName))
                .findFirst()
                .map(UserRecord::getBalance)
                .orElseThrow(() -> new RuntimeException("User not found: " + userName));
    }

    /**
     * Print all users and their balances to console.
     * Useful for debugging and checking balances after transactions.
     */
    public void printAllUserBalances() {
        StreamSupport.stream(userRepository.findAll().spliterator(), false)
                .forEach(u -> System.out.println(u.getName() + ": " + u.getBalance()));
    }
}


