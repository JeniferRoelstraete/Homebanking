package com.mindhub.homebanking.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
public class ClientLoan {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    @ManyToOne()
    @JoinColumn(name = "client_id")
    private Client client;

    @ManyToOne()
    @JoinColumn(name = "loan_id")
    private Loan loan;

    private double amount;

    private Integer payment;

    private double pendingAmount;

    private Integer pendingPayments;

    public ClientLoan() {
    }

    public ClientLoan(Client client, Loan loan, double amount, Integer payment) {
        this.client = client;
        this.loan = loan;
        this.amount = amount;
        this.payment = payment;
        this.pendingAmount = amount;
        this.pendingPayments = payment;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Loan getLoan() {
        return loan;
    }

    public void setLoan(Loan loan) {
        this.loan = loan;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Integer getPayment() {
        return payment;
    }

    public void setPayment(Integer payment) {
        this.payment = payment;
    }

    public double getPendingAmount() {
        return pendingAmount;
    }

    public void setPendingAmount(double pendingAmount) {
        this.pendingAmount = pendingAmount;
    }

    public Integer getPendingPayments() {
        return pendingPayments;
    }

    public void setPendingPayments(Integer pendingPayments) {
        this.pendingPayments = pendingPayments;
    }

}
