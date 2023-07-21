package com.mindhub.homebanking.Services;

import com.mindhub.homebanking.dtos.TransactionDTO;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Set;

public interface TransactionService {
    void save(Transaction transaction);

    public void saveAll(Set<Transaction> transactions);

    Set<TransactionDTO> filterTransactionsByDate(LocalDateTime fromDate, LocalDateTime thruDate, long accountId);
}
