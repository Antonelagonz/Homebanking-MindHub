package com.mindhub.homebanking.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")

    private long id;
    private CardType cardType;
    private CardColor cardColor;
    private String cardNumber, cardHolder;
    private int cvv;
    private LocalDate thruDate, fromDate;

    private Boolean enabled = true;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn (name = "client_id")
    private Client client;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "account_id")
    private Account account;

    @OneToMany(mappedBy = "card", fetch = FetchType.EAGER)
    private Set<Payment> payments;

    public Card (){

    }

    public Card(String cardHolder, Client client, CardType cardType, CardColor cardColor, String cardNumber, int cvv, LocalDate thruDate, LocalDate fromDate, Boolean enabled, Account account) {
        this.cardHolder = cardHolder;
        this.client = client;
        this.cardType = cardType;
        this.cardColor = cardColor;
        this.cardNumber = cardNumber;
        this.cvv = cvv;
        this.fromDate = fromDate;
        this.thruDate = thruDate;
        this.enabled = enabled;
        this.account = account;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCardHolder() { return cardHolder; }

    public void setCardHolder(String cardHolder) {
        this.cardHolder = cardHolder;
    }

    public CardType getCardType() {
        return cardType;
    }

    public void setCardType(CardType cardType) {
        this.cardType = cardType;
    }

    public CardColor getCardColor() {
        return cardColor;
    }

    public void setCardColor(CardColor cardColor) {
        this.cardColor = cardColor;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public int getCvv() {
        return cvv;
    }

    public void setCvv(int cvv) {
        this.cvv = cvv;
    }

    public LocalDate getThruDate() {
        return thruDate;
    }

    public void setThruDate(LocalDate thruDate) {
        this.thruDate = thruDate;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public void setFromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Boolean getEnabled(){ return enabled; }

    public void setEnabled(Boolean enabled){
        this.enabled = enabled;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }


    public Set<Payment> getPayments() {
        return payments;
    }

    public void addPayment(Payment payment) {
        this.payments.add(payment);
    }
    public void setPayments(Set<Payment> payments) {
        this.payments = payments;
    }
}
