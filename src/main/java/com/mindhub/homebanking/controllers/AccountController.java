package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.Services.AccountService;
import com.mindhub.homebanking.Services.ClientService;
import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.AccountType;
import com.mindhub.homebanking.models.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

import static com.mindhub.homebanking.utils.AccountUtils.getAccountNumber;

@RestController
@RequestMapping("/api")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private ClientService clientService;

    @GetMapping("/accounts")
    public List<AccountDTO> getAccounts() {
        return accountService.findAll();
    }

    @GetMapping("/accounts/{id}")
    public AccountDTO getAccount(@PathVariable Long id) {
        return accountService.findById(id);
    }

    @Transactional
    @PostMapping(path = "/accounts")
    public ResponseEntity<Object> createAccount(@RequestParam AccountType type, Authentication authentication) {
       Client client = clientService.findByEmail(authentication.getName());

       if (client.getAccounts().size() == 3) {
           return new ResponseEntity<>("You have reached the account limit that you can have", HttpStatus.FORBIDDEN); //403
                                                //alcanzaste el limite de cuenta que puede tener
       }

       String accountNumber; //declaro una variable para que en bucle cambie su valor
       do {
           accountNumber = getAccountNumber(); // genera un numero aletario y la guardo en el la variable
       } while (accountService.findByNumber(accountNumber) != null); //busca la cuenta por numero y se fija si este numero aletario ya existe

        Account account = new Account(accountNumber, type, LocalDate.now(), 0);

       client.addAccount(account);
       accountService.save(account);
       clientService.save(client);
       return new ResponseEntity<>(HttpStatus.CREATED); //201
    }

    @Transactional
    @DeleteMapping(path = "/accounts/{accountNumber}")
    public ResponseEntity<Object> deleteAccount(@PathVariable String accountNumber) {
        try {
            accountService.deleteByNumber(accountNumber);
        } catch (Exception e) {
            return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
