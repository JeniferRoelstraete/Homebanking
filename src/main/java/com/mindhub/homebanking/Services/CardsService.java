package com.mindhub.homebanking.Services;

import com.mindhub.homebanking.models.Card;

public interface CardsService {
    Card findByNumber(String cardNumber);

    void deleteByNumber(String cardNumber);

    void save(Card card);
}
