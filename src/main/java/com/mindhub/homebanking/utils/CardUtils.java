package com.mindhub.homebanking.utils;

import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.services.CardServices;
import org.springframework.beans.factory.annotation.Autowired;

public final class CardUtils {
    private CardUtils(){}

    @Autowired
    private CardRepository cardRepository;

    public static String getCardNumber(CardRepository cardRepository){
        int min = 1000;
        int max = 9999;
        String cardNumber = (int) ((Math.random()* (max - min)) + min) + " " +
                        (int) ((Math.random()* (max - min)) + min) + " " +
                        (int) ((Math.random()* (max - min)) + min) + " " +
                        (int) ((Math.random()* (max - min)) + min);

        while (cardRepository.findByCardNumber(cardNumber) != null){
            cardNumber = (int) ((Math.random()* (max - min)) + min) + " " +
                     (int) ((Math.random()* (max - min)) + min) + " " +
                     (int) ((Math.random()* (max - min)) + min) + " " +
                     (int) ((Math.random()* (max - min)) + min);
        }


        return cardNumber;
    }

    public static int getCVVNumber(){
        int min = 100;
        int max = 999;
        return (int) ((Math.random() * (max - min)) + min);
    }
}
