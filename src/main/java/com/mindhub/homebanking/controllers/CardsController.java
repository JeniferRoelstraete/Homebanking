package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.Services.CardsService;
import com.mindhub.homebanking.Services.ClientService;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.utils.CardUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.quartz.QuartzTransactionManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.time.LocalDate;

@RestController
@RequestMapping("/api")
public class CardsController {

    @Autowired
    private CardsService cardsService;

    @Autowired
    private ClientService clientService;

    @Transactional
    @PostMapping(path = "/clients/current/cards")
    public ResponseEntity<Object> createCard(@RequestParam CardType type,
                                           @RequestParam CardColor color,
                                           Authentication authentication) { //crear la tarjeta para un  cliente autenticado
        Client client = clientService.findByEmail(authentication.getName());

        if (client.getCards().stream().filter(card -> card.getType().equals(type) && card.getColor().equals(color)).count() == 1) {
            return new ResponseEntity<>("You have reached the card limit for this card type and color", HttpStatus.FORBIDDEN); //403
                                            //alcazado el limite de tarjeta para este tipo y color.
        }

        String cardNumber;
        do {
            cardNumber = CardUtils.getCardNumber();
        } while (cardsService.findByNumber(cardNumber) != null);


        Card card = new Card(
                client.getFirstName() + " " + client.getLastName(),
                type,
                color,
                cardNumber,
                CardUtils.getCvv(),
                LocalDate.now().plusYears(5),
                LocalDate.now(),
                false
        );

        client.addCard(card);
        cardsService.save(card);
        clientService.save(client);

        return new ResponseEntity<>(HttpStatus.CREATED); //201
    }

    @DeleteMapping(path = "/cards/{cardNumber}")
    public ResponseEntity<Object> deleteCard(@PathVariable String cardNumber) {
        try {
            cardsService.deleteByNumber(cardNumber);
        } catch (Exception e) {
            return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }
}

