package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api")
public class CardsController {

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private ClientRepository clientRepository;

    @RequestMapping(path = "/clients/current/cards", method = RequestMethod.POST)
    public ResponseEntity<Object> createCard(@RequestParam CardType type,
                                           @RequestParam CardColor color,
                                           Authentication authentication) { //crear la tarjeta para un  cliente autenticado
        Client client = clientRepository.findByEmail(authentication.getName());

        if (client.getCards().stream().filter(card -> card.getType().equals(type) && card.getColor().equals(color)).count() == 1) {
            return new ResponseEntity<>("You have reached the card limit for this card type and color", HttpStatus.FORBIDDEN); //403
                                            //alcazado el limite de tarjeta para este tipo y color.
        }

        String cardNumber; //declaro una variable para que en bucle cambie su valor
        do {
            cardNumber = Account.getRandomNumber(0, 9999) + "-" + Account.getRandomNumber(0, 9999) + "-" + Account.getRandomNumber(0, 9999) + "-" + Account.getRandomNumber(0, 9999); // genera un numero aletario y la guardo en el la variable
        } while (cardRepository.findByNumber(cardNumber) != null); //busca la tarjeta por numero y se fija si este numero aletario ya exite
        //si encontro tarjeta es distinto de null, vuelve al bucle
        //si no encontro tarjeta con ese numero, se crea tarjeta

        Card card = new Card(
                client.getFirstName() + " " + client.getLastName(),
                type,
                color,
                cardNumber,
                (short) Account.getRandomNumber(0, 999),
                LocalDate.now().plusYears(5),
                LocalDate.now()
        );

        client.addCard(card);
        cardRepository.save(card);
        clientRepository.save(client);

        return new ResponseEntity<>(HttpStatus.CREATED); //201
    }
}

