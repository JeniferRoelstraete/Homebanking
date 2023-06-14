package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;

import java.time.LocalDateTime;

public class TransactionDTO {

    private Long id;

    private TransactionType type;

    private Double ammount;

    private LocalDateTime date;

    private String description;


    public TransactionDTO(Transaction transaction) {
        this.id = transaction.getId();
        this.type = transaction.getType();
        this.ammount = transaction.getAmmount();
        this.date = transaction.getDate();
        this.description = transaction.getDescription();
    }

    public TransactionType getType() {
        return type;
    }

    public Double getAmmount() {
        return ammount;
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
}
