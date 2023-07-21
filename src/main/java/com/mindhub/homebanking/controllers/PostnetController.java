package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.Services.AccountService;
import com.mindhub.homebanking.Services.CardsService;
import com.mindhub.homebanking.Services.ClientService;
import com.mindhub.homebanking.Services.TransactionService;
import com.mindhub.homebanking.dtos.PostnetPaymentDTO;
import com.mindhub.homebanking.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;

@CrossOrigin(origins = "http://127.0.0.1:5500")
@RestController
@RequestMapping("api")
public class PostnetController {

    @Autowired
    CardsService cardsService;

    @Autowired
    ClientService clientService;

    @Autowired
    AccountService accountService;

    @Autowired
    TransactionService transactionService;

    @Transactional
    @PostMapping("process-payment")
    ResponseEntity<Object> processPayment(@RequestBody PostnetPaymentDTO postnetPaymentDTO, Authentication authentication) {

        if (postnetPaymentDTO.getCardNumber().isBlank()) {
            return new ResponseEntity<>("The card number was not found", HttpStatus.BAD_REQUEST);
        }

        if (postnetPaymentDTO.getCvv() == 0) {
            return new ResponseEntity<>("The card cvv was not found", HttpStatus.BAD_REQUEST);
        }

        if (((Double) postnetPaymentDTO.getPaymentAmount()).isNaN()) {
            return new ResponseEntity<>("The payment amount must be null", HttpStatus.BAD_REQUEST);
        }

        if (postnetPaymentDTO.getDescription().isBlank()) {
            return new ResponseEntity<>("The payment description cannot be empty", HttpStatus.BAD_REQUEST);
        }

        Card clientCard = cardsService.findByNumber(postnetPaymentDTO.getCardNumber());

        if (clientCard == null) {
            return new ResponseEntity<>("The card does not belong to the client", HttpStatus.BAD_REQUEST);
        }

        Client client = clientCard.getClient();
        Account debitAccount = client.getAccounts().stream().filter(account -> account.getBalance() > postnetPaymentDTO.getPaymentAmount()).findFirst().orElse(null);

        if (postnetPaymentDTO.getPaymentAmount() <= 0) {
            return new ResponseEntity<>("The payment amount must be positive", HttpStatus.BAD_REQUEST);
        }

        if (debitAccount == null) {
            return new ResponseEntity<>("The clients has no accounts with available amount to process the payment", HttpStatus.BAD_REQUEST);
        }

        if (!clientCard.getCvv().equals(postnetPaymentDTO.getCvv())) {
            return new ResponseEntity<>("The card cvv number is incorrect", HttpStatus.BAD_REQUEST);
        }

        if (LocalDate.now().isAfter(clientCard.getThruDate())) {
            return new ResponseEntity<>("The card is expired", HttpStatus.BAD_REQUEST);
        }

        double newAccountBalance = debitAccount.getBalance() - postnetPaymentDTO.getPaymentAmount();
        debitAccount.setBalance(newAccountBalance);

        Transaction transaction = new Transaction(TransactionType.DEBITO, postnetPaymentDTO.getPaymentAmount(), LocalDateTime.now(), postnetPaymentDTO.getDescription(), newAccountBalance);
        debitAccount.addTransaction(transaction);

        transactionService.save(transaction);
        accountService.save(debitAccount);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
