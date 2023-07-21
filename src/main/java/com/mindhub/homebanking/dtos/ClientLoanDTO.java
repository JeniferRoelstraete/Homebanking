package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.ClientLoan;
import com.mindhub.homebanking.models.Loan;

public class ClientLoanDTO {
    private long clientLoanId;

    private long loanId;

    private String loanName;

    private double amount;

    private Integer payment;

    private Integer interestRate;

    private double pendingAmount;

    private Integer pendingPayments;

    public ClientLoanDTO(ClientLoan clientLoan) {
        this.clientLoanId = clientLoan.getId();
        this.loanId = clientLoan.getLoan().getId();
        this.loanName = clientLoan.getLoan().getName();
        this.amount = clientLoan.getAmount();
        this.payment = clientLoan.getPayment();
        this.interestRate = clientLoan.getLoan().getInterestRate();
        this.pendingAmount = clientLoan.getPendingAmount();
        this.pendingPayments = clientLoan.getPendingPayments();
    }

    public long getClientLoanId() {
        return clientLoanId;
    }

    public long getLoanId() {
        return loanId;
    }

    public String getLoanName() {
        return loanName;
    }

    public double getAmount() {
        return amount;
    }

    public Integer getPayment() {
        return payment;
    }

    public double getPendingAmount() {
        return pendingAmount;
    }

    public Integer getPendingPayments() {
        return pendingPayments;
    }

    public Integer getInterestRate() {
        return interestRate;
    }
}
