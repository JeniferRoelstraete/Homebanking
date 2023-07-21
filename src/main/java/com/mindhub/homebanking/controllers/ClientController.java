package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.Services.AccountService;
import com.mindhub.homebanking.Services.ClientService;
import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.AccountType;
import com.mindhub.homebanking.models.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

import static com.mindhub.homebanking.utils.NumberUtils.getRandomNumber;

@RestController
@RequestMapping("/api")
public class ClientController {


    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ClientService clientService;

    @Autowired
    private AccountService accountService;

    @GetMapping("/clients") //api/clients
    public List<ClientDTO> getClients() {
        return clientService.findAll();
    }

    @GetMapping("/clients/{id}") //api/clients/id
    public ClientDTO getClient(@PathVariable Long id){
        return clientService.getClientById(id);
    }

    @GetMapping("/clients/current")
    public ClientDTO getCurrentClient(Authentication authentication) {
        return clientService.findDTOByEmail(authentication.getName());
    }

            //un registro nuevo,cliente nuevo
    @Transactional
    @PostMapping(path = "/clients")
    public ResponseEntity<Object> register(@RequestParam String firstName,
                                           @RequestParam String lastName,
                                           @RequestParam String email,
                                           @RequestParam String password) {
        if (firstName.isBlank() || lastName.isBlank() || email.isBlank() || password.isBlank()) {
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN); //403
        }                                   //no se encontraron los datos


        if (clientService.findByEmail(email) !=  null) {
            return new ResponseEntity<>("Email already in use", HttpStatus.FORBIDDEN);
        }                                      //el email ya esta en uso

        String accountNumber;
        do {
            accountNumber = "VIN-" + getRandomNumber(1, 99999999);
        } while (accountService.findByNumber(accountNumber) != null);

        Account newAccount = new Account(accountNumber, AccountType.SAVINGS_ACCOUNT, LocalDate.now(), 0);
        Client newClient = new Client(firstName, lastName, email, passwordEncoder.encode(password));

        newClient.addAccount(newAccount);

        clientService.save(newClient);
        accountService.save(newAccount);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }


}
