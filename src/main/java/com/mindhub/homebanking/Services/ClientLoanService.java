package com.mindhub.homebanking.Services;

import com.mindhub.homebanking.dtos.ClientLoanDTO;
import com.mindhub.homebanking.models.ClientLoan;

import java.util.Set;

public interface ClientLoanService {
    void save(ClientLoan clientLoan);

    Set<ClientLoanDTO> findByClientId(long clientId);

    boolean performPayment(String originaAccountNumber, long clientLoanId);
}
