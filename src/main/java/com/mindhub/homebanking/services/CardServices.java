package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dto.CardDTO;
import com.mindhub.homebanking.models.Card;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface CardServices {


    CardDTO findCardById(@PathVariable Long id);

    void saveCard(Card card);

    Card findByCardNumber(String number);

    void deleteCard(Card card);
}
