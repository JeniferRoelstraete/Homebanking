package com.mindhub.homebanking.Services;

import com.itextpdf.text.Document;
import com.mindhub.homebanking.dtos.TransactionDTO;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;

import java.util.Set;

public interface PDFGeneratorService {
    byte[] generateTransactionsPDF(String accountNumber, Set<TransactionDTO> transactions);

}
