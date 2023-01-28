package com.mindhub.homebanking;


import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.services.CardServices;
import com.mindhub.homebanking.utils.CardUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
public class CardUtilsTest {



   /* @Autowired
    CardRepository cardRepository;

    @Test
    public void cardNumberIsCreated(){
        String cardNumber = CardUtils.getCardNumber(cardRepository);
        assertThat(cardNumber, is(not(emptyOrNullString())));
    }

    @Test
    public void cardCVVIsCreated(){
        int cardCVV = CardUtils.getCVVNumber();
        assertThat(cardCVV, notNullValue());
    }*/
}
