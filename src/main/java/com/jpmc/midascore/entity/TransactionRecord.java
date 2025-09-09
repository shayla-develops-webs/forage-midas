package com.jpmc.midascore.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "transaction_records")
public class TransactionRecord {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", nullable = false)
    private UserRecord sender;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipient_id", nullable = false)
    private UserRecord recipient;
    
    @Column(nullable = false)
    private Float amount;
    
    @Column(nullable = false)
    private LocalDateTime timestamp;

    @Column(nullable = false)
    private Float incentive = 0f; // new field for Incentive API
    
    // Constructors
    public TransactionRecord() {
        this.timestamp = LocalDateTime.now();
    }
    
    public TransactionRecord(UserRecord sender, UserRecord recipient, Float amount) {
        this.sender = sender;
        this.recipient = recipient;
        this.amount = amount;
        this.timestamp = LocalDateTime.now();
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public UserRecord getSender() {
        return sender;
    }
    
    public void setSender(UserRecord sender) {
        this.sender = sender;
    }
    
    public UserRecord getRecipient() {
        return recipient;
    }
    
    public void setRecipient(UserRecord recipient) {
        this.recipient = recipient;
    }
    
    public Float getAmount() {
        return amount;
    }
    
    public void setAmount(Float amount) {
        this.amount = amount;
    }
    
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public Float getIncentive() {
        return incentive;
    }

    public void setIncentive(Float incentive) {
        this.incentive = incentive;
    }
    
    @Override
    public String toString() {
        return "TransactionRecord{id=" + id + 
               ", sender=" + (sender != null ? sender.getName() : "null") + 
               ", recipient=" + (recipient != null ? recipient.getName() : "null") + 
               ", amount=" + amount + 
               ", incentive=" + incentive +
               ", timestamp=" + timestamp + "}";
    }
}
