package com.mindhub.homebanking.dtos;

public class PostnetPaymentDTO {
    private String cardNumber;

    private short cvv;

    private double paymentAmount;

    private String description;

    public String getCardNumber() {
        return cardNumber;
    }

    public short getCvv() {
        return cvv;
    }

    public double getPaymentAmount() {
        return paymentAmount;
    }

    public String getDescription() {
        return description;
    }
}
