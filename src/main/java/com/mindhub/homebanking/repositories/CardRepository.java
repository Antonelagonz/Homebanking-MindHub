package com.mindhub.homebanking.repositories;


import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Collection;
import java.util.List;

@RepositoryRestResource
public interface CardRepository extends JpaRepository <Card, Long> {

    List<Card> findByClientAndCardType(Client client, CardType cardType);

    public Card findByCardNumber(String cardNumber);

}
