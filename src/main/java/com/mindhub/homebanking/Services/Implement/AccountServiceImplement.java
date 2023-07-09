package com.mindhub.homebanking.Services.Implement;

import com.mindhub.homebanking.Services.AccountService;
import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountServiceImplement implements AccountService {

    @Autowired
    private AccountRepository accountRepository;
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
    public void save(Account account) {
        accountRepository.save(account);
    }
}
