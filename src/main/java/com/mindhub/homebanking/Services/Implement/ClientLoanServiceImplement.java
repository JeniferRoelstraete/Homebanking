package com.mindhub.homebanking.Services.Implement;

import com.mindhub.homebanking.Services.ClientLoanService;
import com.mindhub.homebanking.models.ClientLoan;
import com.mindhub.homebanking.repositories.ClientLoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClientLoanServiceImplement implements ClientLoanService {

    @Autowired
    private ClientLoanRepository clientLoanRepository;

    @Override
    public void save(ClientLoan clientLoan) {
        clientLoanRepository.save(clientLoan);
    }
}
