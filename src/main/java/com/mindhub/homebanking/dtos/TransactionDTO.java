package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;

import java.time.LocalDateTime;

public class TransactionDTO {

    private Long id;

    private TransactionType type;

    private Double amount;

    private LocalDateTime date;

    private String description;

    private String originAccountNumber;

    private String destinationAccountNumber;

    private Double currentBalance;

    public TransactionDTO() { }

    public TransactionDTO(Transaction transaction) {
        this.id = transaction.getId();
        this.type = transaction.getType();
        this.amount = transaction.getAmount();
        this.date = transaction.getDate();
        this.description = transaction.getDescription();
        this.currentBalance = transaction.getCurrentBalance();
    }

    public TransactionType getType() {
        return type;
    }

    public Double getAmount() {
        return amount;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public Long getId() {
        return id;
    }

    public String getOriginAccountNumber() {
        return originAccountNumber;
    }

    public String getDestinationAccountNumber() {
        return destinationAccountNumber;
    }

    public Double getCurrentBalance() {
        return currentBalance;
    }
}
