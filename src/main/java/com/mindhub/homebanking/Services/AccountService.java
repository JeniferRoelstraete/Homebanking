package com.mindhub.homebanking.Services;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public interface AccountService {
    List<AccountDTO> findAll();

    AccountDTO findById(long id);

    Account findByNumber(String accountNumber);

    void save(Account account);
}
