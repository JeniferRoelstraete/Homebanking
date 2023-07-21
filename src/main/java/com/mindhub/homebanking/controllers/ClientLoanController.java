package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.Services.*;
import com.mindhub.homebanking.dtos.ClientLoanDTO;
import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api")
public class ClientLoanController {

    @Autowired
    private ClientLoanService clientLoanService;

    @Autowired
    private ClientService clientService;

    @GetMapping(path = "/clientLoans")
    public ResponseEntity<Object> getClientLoans(Authentication authentication) {

        Client client = clientService.findByEmail(authentication.getName());

        Set<ClientLoanDTO> clientLoan = clientLoanService.findByClientId(client.getId());

        return new ResponseEntity<>(clientLoan, HttpStatus.CREATED);
    }


    @Transactional
    @PostMapping(path = "/clientLoans")
    public ResponseEntity<Object> loanPayment(@RequestParam String originAccountNumber, @RequestParam long clientLoanId) {
        boolean successfulPayment = clientLoanService.performPayment(originAccountNumber, clientLoanId);

        if (successfulPayment) {
            return new ResponseEntity<>("Payment successful", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Payment error, transaction was aborted!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
