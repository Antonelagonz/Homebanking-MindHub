package com.mindhub.homebanking.services.implementation;


import com.mindhub.homebanking.dto.CardDTO;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.services.CardServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

@Service
public class CardServicesImplementation implements CardServices {

    @Autowired
    CardRepository cardRepository;


    @Override
    public CardDTO findCardById(@PathVariable Long id){
        return cardRepository.findById(id).map(card -> new CardDTO(card)).orElse(null);
    }
    @Override
    public void saveCard(Card card){
        cardRepository.save(card);
    }


    @Override
    public Card findByCardNumber(String number){
        return cardRepository.findByCardNumber(number);
    }

    @Override
    public void deleteCard(Card card) {
        cardRepository.delete(card);
    }
}
