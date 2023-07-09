package com.mindhub.homebanking.Services.Implement;

import com.mindhub.homebanking.Services.CardsService;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CardsServiceImplement implements CardsService {

    @Autowired
    private CardRepository cardRepository;
    @Override
    public Card findByNumber(String CardNumber) {
        return cardRepository.findByNumber(CardNumber);
    }

    @Override
    public void save(Card card) {
        cardRepository.save(card);

    }
}
