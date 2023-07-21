package com.mindhub.homebanking.Services.Implement;

import com.mindhub.homebanking.Services.AccountService;
import com.mindhub.homebanking.Services.TransactionService;
import com.mindhub.homebanking.dtos.TransactionDTO;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TransactionServiceImplement implements TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Override
    public void save(Transaction transaction) {
        transactionRepository.save(transaction);
    }

    @Override
    public void saveAll(Set<Transaction> transactions) {
        //transactionRepository.saveAll(transactions);
    }

    @Override
    public Set<TransactionDTO> filterTransactionsByDate(LocalDateTime fromDate, LocalDateTime thruDate, long accountId) {
        Set<Transaction> accountTransactions = transactionRepository.findByAccountId(accountId);
        return accountTransactions.stream().map(transaction -> new TransactionDTO(transaction)).filter(transaction -> transaction.getDate().isAfter(fromDate) && transaction.getDate().isBefore(thruDate)).collect(Collectors.toSet());
    }

}
