package com.mindhub.homebanking.controllers;

import com.itextpdf.text.Document;
import com.mindhub.homebanking.Services.AccountService;
import com.mindhub.homebanking.Services.ClientService;
import com.mindhub.homebanking.Services.PDFGeneratorService;
import com.mindhub.homebanking.Services.TransactionService;
import com.mindhub.homebanking.dtos.TransactionDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class TransactionController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private PDFGeneratorService pdfGeneratorService;

    @Transactional
    @PostMapping(path = "/client/current/transactions")
    public ResponseEntity<Object> register(@RequestBody TransactionDTO transactionDto, Authentication authentication) {
        String originAccountNumber = transactionDto.getOriginAccountNumber();
        String destinationAccountNumber = transactionDto.getDestinationAccountNumber();
        Double transactionAmount = transactionDto.getAmount();
        String description = transactionDto.getDescription();

        if (transactionAmount.isNaN() || originAccountNumber.isBlank() || destinationAccountNumber.isBlank() || description.isBlank()) {
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }                                      //faltan datos

        if (originAccountNumber.equals(destinationAccountNumber)) {
            return new ResponseEntity<>("Destination and origin accounts are the same", HttpStatus.FORBIDDEN);
        }                                       //la cuenta de origen son las mismas

        Account originAccount = accountService.findByNumber(originAccountNumber);

        if (originAccount == null) {
            return new ResponseEntity<>("Origin account does not exist", HttpStatus.FORBIDDEN);
        }                               //la cuenta de origen no existe

        Client client = clientService.findByEmail(authentication.getName());

        if (!client.getAccounts().contains(originAccount)) {
            return new ResponseEntity<>("Origin account does not belong to client accounts", HttpStatus.FORBIDDEN);
        }                                       //la cuenta de origen no pertence a la cuenta del cliente

        Account destinationAccount = accountService.findByNumber(destinationAccountNumber);

        if (destinationAccount == null) {
            return new ResponseEntity<>("Destination account does not exist", HttpStatus.FORBIDDEN);
        }                                       //la cuenta de destino no existe

        if (transactionAmount <= 0) {
            return new ResponseEntity<>("The transaction amount must be positive", HttpStatus.FORBIDDEN);
        }

        if (originAccount.getBalance() < transactionAmount) {
            return new ResponseEntity<>("Account balance is not enough for performing the transaction", HttpStatus.FORBIDDEN);
        }                                           //el balance de la cuenta no es sufiente para realizar la trancaccion

        double finalOriginAccountBalance = originAccount.getBalance() - transactionAmount;
        originAccount.setBalance(finalOriginAccountBalance);

        double finalDestinationAccountBalance = destinationAccount.getBalance() + transactionAmount;
        destinationAccount.setBalance(finalDestinationAccountBalance);

        Transaction debitTransaction = new Transaction(TransactionType.DEBITO, -1 * transactionAmount, LocalDateTime.now(), description, finalOriginAccountBalance);
        Transaction creditTransaction = new Transaction(TransactionType.CREDITO, transactionAmount, LocalDateTime.now(), description, finalDestinationAccountBalance);

        transactionService.save(debitTransaction);
        transactionService.save(creditTransaction);

        originAccount.addTransaction(debitTransaction);
        destinationAccount.addTransaction(creditTransaction);

        accountService.save(originAccount);
        accountService.save(destinationAccount);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping(path = "/transactions")
    public ResponseEntity<Object> getTransactions(
            @RequestParam() String accountNumber,
            @RequestParam() @DateTimeFormat(pattern = "yyyy-MM-dd") Date fromDate,
            @RequestParam() @DateTimeFormat(pattern = "yyyy-MM-dd") Date thruDate,
            @RequestParam() boolean printPDF) {

        if (fromDate == null || thruDate == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Account clientAccount = accountService.findByNumber(accountNumber);

        LocalDateTime from = LocalDateTime.ofInstant(
                fromDate.toInstant(), ZoneId.systemDefault());
        LocalDateTime thru = LocalDateTime.ofInstant(
                thruDate.toInstant(), ZoneId.systemDefault()).plusDays(1);

        Set<TransactionDTO> clientTransactions = transactionService.filterTransactionsByDate(from, thru, clientAccount.getId());

        if (printPDF) {
            byte[] generatedPDF = pdfGeneratorService.generateTransactionsPDF(accountNumber, clientTransactions);
            return new ResponseEntity<>(generatedPDF, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(clientTransactions, HttpStatus.OK);
        }
    }
}
