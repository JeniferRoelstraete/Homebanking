package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.Services.AccountService;
import com.mindhub.homebanking.Services.ClientService;
import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private ClientService clientService;

    @RequestMapping("/accounts")
    public List<AccountDTO> getAccounts() {
        return accountService.findAll();
    }

    @RequestMapping("/accounts/{id}")
    public AccountDTO getAccount(@PathVariable Long id) {
        return accountService.findById(id);
    }

    @RequestMapping(path = "/clients/current/accounts" , method = RequestMethod.POST)
    public ResponseEntity<Object> createAccount(Authentication authentication) {
       Client client = clientService.findByEmail(authentication.getName());

       if (client.getAccounts().size() == 3) {
           return new ResponseEntity<>("You have reached the account limit that you can have", HttpStatus.FORBIDDEN); //403
                                                //alcanzaste el limite de cuenta que puede tener
       }

       String accountNumber; //declaro una variable para que en bucle cambie su valor
       do {
           accountNumber = "VIN-" + Account.getRandomNumber(1, 99999999); // genera un numero aletario y la guardo en el la variable
       } while (accountService.findByNumber(accountNumber) != null); //busca la cuenta por numero y se fija si este numero aletario ya existe

       Account account = new Account(accountNumber, LocalDate.now(), 0); //si no existe la creo

       client.addAccount(account);
       accountService.save(account);
       clientService.save(client);
       return new ResponseEntity<>(HttpStatus.CREATED); //201
    }

}
