package com.mindhub.homebanking.Services.Implement;

import com.mindhub.homebanking.Services.AccountService;
import com.mindhub.homebanking.Services.ClientLoanService;
import com.mindhub.homebanking.Services.TransactionService;
import com.mindhub.homebanking.dtos.ClientLoanDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.ClientLoan;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.repositories.ClientLoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ClientLoanServiceImplement implements ClientLoanService {

    @Autowired
    private ClientLoanRepository clientLoanRepository;

    @Autowired
    private AccountService accountService;

    @Autowired
    private TransactionService transactionService;

    @Override
    public void save(ClientLoan clientLoan) {
        clientLoanRepository.save(clientLoan);
    }

    @Override
    public Set<ClientLoanDTO> findByClientId(long clientId) {
        return clientLoanRepository.findAll().stream()
                .filter(clientLoan -> clientId == clientLoan.getClient().getId())
                .map(clientLoan -> new ClientLoanDTO(clientLoan)).collect(Collectors.toSet());
    }

    @Override
    @Transactional
    public boolean performPayment(String originaAccountNumber, long clientLoanId) {
        ClientLoan clientLoan = clientLoanRepository.findById(clientLoanId).orElse(null);
        if (clientLoan != null) {
            Account originAccount = accountService.findByNumber(originaAccountNumber);
            double amountToSubtract = clientLoan.getAmount() / clientLoan.getPayment();

            clientLoan.setPendingAmount(clientLoan.getPendingAmount() - amountToSubtract);
            clientLoan.setPendingPayments(clientLoan.getPendingPayments() - 1);
            originAccount.setBalance(originAccount.getBalance() - amountToSubtract);

            Transaction paymentTransaction = new Transaction(TransactionType.DEBITO, amountToSubtract, LocalDateTime.now(), "Loan payment transaction", originAccount.getBalance());
            originAccount.addTransaction(paymentTransaction);

            save(clientLoan);
            transactionService.save(paymentTransaction);
            accountService.save(originAccount);

            return true;
        } else {
            return false;
        }
    }
}
