package com.mindhub.homebanking.Services;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Card;

public interface CardsService {
    Card findByNumber(String CardNumber);

    void save(Card card);
}
