package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dto.CardDTO;
import com.mindhub.homebanking.dto.ClientDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.services.AccountServices;
import com.mindhub.homebanking.services.CardServices;
import com.mindhub.homebanking.services.ClientServices;
import com.mindhub.homebanking.utils.CardUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import javax.swing.text.html.Option;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api")
public class CardController {
    @Autowired
    private CardServices cardServices;

    @Autowired
    private ClientServices clientServices;
    @Autowired
    private AccountServices accountServices;

    @Autowired
    private CardRepository cardRepository;

    @RequestMapping("/clients/{id}/cards")
    public ClientDTO getClientDTO(@PathVariable Long id) {
        return clientServices.getClientDTO(id);
    }

    @RequestMapping("/cards/{id}")
    public CardDTO findCardById(@PathVariable Long id) {
        return cardServices.findCardById(id);
    }

    @PostMapping("/client/current/cards") //el cliente envia los datos a esta url
    public ResponseEntity<Object> createCard(@RequestParam CardType cardType,
                                             @RequestParam CardColor cardColor,
                                             @RequestParam String account,
                                             Authentication authentication) {
        Client client = clientServices.findByEmail(authentication.getName());
        Set<Card> cards = client.getCards();
        List<Card> cardsEnabled = cards.stream().filter(card -> card.getEnabled().equals(true)).filter(card -> card.getCardType() == cardType).collect(Collectors.toList());
        Account accountCard = accountServices.findByNumber(account);

        if (cardType.equals(null)) {
            return new ResponseEntity<>("The card type field can't be empty", HttpStatus.BAD_REQUEST);
        }
        if (cardColor.equals(null)) {
            return new ResponseEntity<>("The card color field can't be empty", HttpStatus.BAD_REQUEST);
        }

        if (cardsEnabled.size() == 6) {
            return new ResponseEntity<>("You have reached the limit of cards", HttpStatus.FORBIDDEN);
        } else if (cardsEnabled.equals(CardType.CREDIT)) {
            if ((cardsEnabled.stream().filter(card -> card.getCardType().equals(CardType.CREDIT)).collect(Collectors.toSet()).size() >= 3)) {
                return new ResponseEntity<>("You have reached the max amount of Credit Cards", HttpStatus.FORBIDDEN);
            }
        } else if (cardsEnabled.equals(CardType.DEBIT)) {
            if (cardsEnabled.stream().filter(card -> card.getCardType().equals(CardType.DEBIT)).collect(Collectors.toSet()).size() >= 3) {
                return new ResponseEntity<>("You have reached the max amount of Debit Cards", HttpStatus.FORBIDDEN);
            }
        } else if (cardsEnabled.stream().filter(card -> card.getCardColor() == cardColor).count() == 1) {
            return new ResponseEntity<>("You already have a card with this properties", HttpStatus.CONFLICT);
        }
            Card card = new Card(client.getFirstName() + " " + client.getLastName(), client, cardType, cardColor, CardUtils.getCardNumber(cardRepository), CardUtils.getCVVNumber(), LocalDate.now(), LocalDate.now().plusYears(5), true, accountCard);
            client.addCard(card);
            cardServices.saveCard(card);
            return new ResponseEntity<>("Your card has been created successfully", HttpStatus.CREATED);

    }

    @PatchMapping("/client/current/cards/disable")
    public ResponseEntity<Object> disableCard(@RequestParam String cardNumber, Authentication authentication){
        Client client = this.clientServices.findByEmail(authentication.getName());
        Card cardToDisable = this.cardServices.findByCardNumber(cardNumber);

        if (!client.getCards().contains(cardToDisable)){
            return new ResponseEntity<>("The card you want to delete doesn't belong to the client.", HttpStatus.FORBIDDEN);
        }
        if (cardToDisable == null){
            return new ResponseEntity<>("The card doesn't exist", HttpStatus.FORBIDDEN);
        }

        cardToDisable.setEnabled(false);
        cardServices.saveCard(cardToDisable);
        return new ResponseEntity<>("The card had been deleted successfully", HttpStatus.OK);
    }
}

