package com.mindhub.homebanking;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
public class RepositoriesTest {
    @Autowired
    AccountRepository accountRepository;

    @Autowired
    CardRepository cardRepository;

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    LoanRepository loanRepository;

    @Autowired
    TransactionRepository transactionRepository;

    @Test
    public void existAccount() {
        List<Account> accounts = accountRepository.findAll();
        assertThat(accounts, is(not(empty())));
    }

    @Test
    public void existAccountWithPositiveBalance() {
        List<Account> accounts = accountRepository.findAll();
        assertThat(accounts, hasItem(hasProperty("balance", is(greaterThan(0.0)))));
    }

    @Test
    public void existCard() {
        List<Card> cards = cardRepository.findAll();
        assertThat(cards, is(not(empty())));
    }

    @Test
    public void noneCardWithoutCvv() {
        List<Card> cards = cardRepository.findAll();
        assertThat(cards, everyItem(hasProperty("cvv", not(empty()))));
    }

    @Test
    public void existClient() {
        List<Client> clients = clientRepository.findAll();
        assertThat(clients, is(not(empty())));
    }

    @Test
    public void clientLastNameNotEmpty() {
        List<Client> clients = clientRepository.findAll();
        assertThat(clients,everyItem(hasProperty("lastName",not(empty()))));
    }

    @Test
    public void existLoans() {
        List<Loan> loans = loanRepository.findAll();
        assertThat(loans, is(not(empty())));
    }

    @Test
    public void existPersonalLoan() {
        List<Loan> loans = loanRepository.findAll();
        assertThat(loans, hasItem(hasProperty("name", is("Personal"))));
    }

    @Test
    public void existTransaction() {
        List<Transaction> transactions = transactionRepository.findAll();
        assertThat(transactions, is(not(empty())));
    }

    @Test
    public void existCreditTransaction() {
        List<Transaction> transactions = transactionRepository.findAll();
        assertThat(transactions, hasItem(hasProperty("type", is(TransactionType.CREDITO))));
    }
}
