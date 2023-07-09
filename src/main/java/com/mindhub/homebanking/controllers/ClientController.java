package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.Services.AccountService;
import com.mindhub.homebanking.Services.ClientService;
import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ClientController {


    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ClientService clientService;

    @Autowired
    private AccountService accountService;

    @RequestMapping("/clients") //api/clients
    public List<ClientDTO> getClients() {
        return clientService.findAll();
    }

    @RequestMapping("/clients/{id}") //api/clients/id
    public ClientDTO getClient(@PathVariable Long id){
        return clientService.getClientById(id);
    }

    @RequestMapping("/clients/current")
    public ClientDTO getCurrentClient(Authentication authentication) {
        return clientService.findDTOByEmail(authentication.getName());
    }

            //un registro nuevo,cliente nuevo
    @RequestMapping(path = "/clients", method = RequestMethod.POST)
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

        String accountNumber; //declaro una variable para que en bucle cambie su valor
        do {
            accountNumber = "VIN-" + Account.getRandomNumber(1, 99999999); // genera un numero aletario y la guardo en el la variable
        } while (accountService.findByNumber(accountNumber) != null); //busca la cuenta por numero y se fija si este numero aletario ya existe

        Account newAccount = new Account(accountNumber, LocalDate.now(), 0);
        Client newClient = new Client(firstName, lastName, email, passwordEncoder.encode(password));

        newClient.addAccount(newAccount);

        clientService.save(newClient);
        accountService.save(newAccount);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }


}
