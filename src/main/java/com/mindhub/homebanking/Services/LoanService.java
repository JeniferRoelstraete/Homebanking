package com.mindhub.homebanking.Services;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Loan;

import java.util.List;

public interface LoanService {

    Loan findById(long id);

    List<LoanDTO> findAll();
}
