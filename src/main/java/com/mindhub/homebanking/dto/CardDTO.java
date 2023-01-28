package com.mindhub.homebanking.dto;

import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.Client;

import java.time.LocalDate;

public class CardDTO {

    private long id;
    private CardType cardType;
    private CardColor cardColor;
    private String cardNumber, cardHolder;
    private int cvv;
    private LocalDate thruDate, fromDate;

    private Boolean enabled = true;


    public CardDTO(Card card) {
        this.id = card.getId();
        this.cardHolder = card.getCardHolder();
        this.cardType = card.getCardType();
        this.cardColor = card.getCardColor();
        this.cardNumber = card.getCardNumber();
        this.cvv = card.getCvv();
        this.fromDate = card.getFromDate();
        this.thruDate = card.getThruDate();
        this.enabled = card.getEnabled();
    }

    public long getId() { return id; }

    public CardType getCardType() { return cardType; }

    public CardColor getCardColor() { return cardColor; }

    public String getCardNumber() { return cardNumber; }

    public int getCvv() { return cvv; }

    public LocalDate getThruDate() { return thruDate; }

    public LocalDate getFromDate() { return fromDate; }

    public String getCardHolder() { return cardHolder; }

    public Boolean getEnabled(){ return enabled; }
}
