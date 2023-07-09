package com.mindhub.homebanking.Services.Implement;

import com.mindhub.homebanking.Services.TransactionService;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionServiceImplement implements TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;
    @Override
    public void save(Transaction transaction) {
        transactionRepository.save(transaction);
    }
}
