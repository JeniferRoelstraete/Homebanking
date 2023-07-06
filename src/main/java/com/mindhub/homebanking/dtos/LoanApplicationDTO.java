package com.mindhub.homebanking.dtos;

//dto solicita el prestamo
public class LoanApplicationDTO {

    private Long id;

    private Double amount;

    private Integer payments;

    private String destinationAccount;

    public LoanApplicationDTO(){};

    public LoanApplicationDTO(Long loanId, Double amount, Integer payments, String destinationAccount) {
        this.id = loanId;
        this.amount = amount;
        this.payments = payments;
        this.destinationAccount = destinationAccount;
    }

    public Long getId() {
        return id;
    }

    public Double getAmount() {
        return amount;
    }

    public Integer getPayments() {
        return payments;
    }

    public String getDestinationAccount() {
        return destinationAccount;
    }
}
