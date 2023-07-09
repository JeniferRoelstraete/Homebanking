package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.Services.*;
import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.Id;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class LoanController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private ClientLoanService clientLoanService;

    @Autowired
    private LoanService loanService;

    @Autowired
    private TransactionService transactionService;

    @Transactional
    @RequestMapping (path ="/loans", method = RequestMethod.POST)
    public ResponseEntity<Object> createLoans(@RequestBody LoanApplicationDTO loanApplicationDTO,Authentication authentication){
        if (loanApplicationDTO.getId() == null ||
                loanApplicationDTO.getAmount() == null ||
                loanApplicationDTO.getAmount() <= 0 ||
                loanApplicationDTO.getPayments() == null ||
                loanApplicationDTO.getPayments() <= 0 ||
                loanApplicationDTO.getDestinationAccount().isBlank()) {
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }

        Loan loan = loanService.findById(loanApplicationDTO.getId());

        if (loan == null) {
            return new ResponseEntity<>("Loan type does not exist", HttpStatus.FORBIDDEN);
        }

        if (loanApplicationDTO.getAmount() > loan.getMaxAmount()) {
            return new ResponseEntity<>("The requested amount exceeds the loan type maximum amount", HttpStatus.FORBIDDEN);
        }

        if (!loan.getPayments().contains(loanApplicationDTO.getPayments())) {
            return new ResponseEntity<>("The quantity of payments is not available for the selected loan type", HttpStatus.FORBIDDEN);
        }           // La cantidad de pagos no está disponible para el tipo de préstamo seleccionado

        Account account = accountService.findByNumber(loanApplicationDTO.getDestinationAccount());

        if (account == null) {
            return new ResponseEntity<>("Destination account does not exist", HttpStatus.FORBIDDEN);
        }

        Client client = clientService.findByEmail(authentication.getName());

        if (!client.getAccounts().contains(account)) {
            return new ResponseEntity<>("Destination account does not belong to client accounts", HttpStatus.FORBIDDEN);
        }

        ClientLoan newLoan = new ClientLoan(client, loan, loanApplicationDTO.getAmount() * 120/100, loanApplicationDTO.getPayments());
        Transaction newTransaction = new Transaction(TransactionType.CREDITO, loanApplicationDTO.getAmount(), LocalDateTime.now(), loan.getName() + " loan approved");

        // Actualizo balance de la cuenta con el monto a recibir del préstamo
        account.setBalance(account.getBalance() + loanApplicationDTO.getAmount());

        client.addClientLoan(newLoan);
        account.addTransaction(newTransaction);

        clientLoanService.save(newLoan);
        transactionService.save(newTransaction);
        accountService.save(account);
        clientService.save(client);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @RequestMapping("/loans")
    public List<LoanDTO> getLoans() {
       return loanService.findAll();
    }
}
