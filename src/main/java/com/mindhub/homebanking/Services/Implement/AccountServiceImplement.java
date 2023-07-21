package com.mindhub.homebanking.Services.Implement;

import com.mindhub.homebanking.Services.AccountService;
import com.mindhub.homebanking.Services.TransactionService;
import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AccountServiceImplement implements AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionService transactionService;

    @Override
    public List<AccountDTO> findAll() {
        return accountRepository.findAll().stream().map(account -> new AccountDTO(account)).collect(Collectors.toList());
    }

    @Override
    public AccountDTO findById(long id) {
        return new AccountDTO(accountRepository.findById(id).orElse(null));
    }

    @Override
    public Account findByNumber(String accountNumber){
        return accountRepository.findByNumber(accountNumber);
    }

    @Override
    public void deleteByNumber(String accountNumber) {
        Account accountToBeDeleted = accountRepository.findByNumber(accountNumber);
        Set<Transaction> accountTransactions = accountToBeDeleted.getTransactions();

        accountToBeDeleted.setDeleted(true);
        accountTransactions.forEach(transaction -> transaction.setDeleted(true));

        transactionService.saveAll(accountTransactions);
        accountRepository.save(accountToBeDeleted);
    }

    @Override
    public void save(Account account) {
        accountRepository.save(account);
    }
}
