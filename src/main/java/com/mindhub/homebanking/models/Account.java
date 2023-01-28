package com.mindhub.homebanking.models;


import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Account {
    @Id         //primary key
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    // va a generar un valor, utiliza la estrategia de la base de datos, generator
    @GenericGenerator(name = "native", strategy = "native") //se fija que los ids no sean iguales
    private long id;
    private String number;
    private LocalDateTime creationDate;
    private double balance;
    private Boolean enabled = true;

    private AccountType accountType;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn (name = "client_id")
    private Client client;

    @OneToMany(mappedBy = "account", fetch = FetchType.EAGER)
    Set<Transaction> transactions = new HashSet<>(); //hashSet crea una nueva lista de accounts sin repetir

    public Account() {
    }

    public Account(String number, LocalDateTime creationDate, double balance, Client client, Boolean enabled, AccountType accountType){
        this.number = number;
        this.creationDate = creationDate;
        this.balance = balance;
        this.client = client;
        this.enabled = enabled;
        this.accountType = accountType;
    }


    public long getId() { return id; }

    public void setId(long id) { this.id = id; }

    public String getNumber(){ return number; }

    public void setNumber(String number){
        this.number = number;
    }

    public LocalDateTime getCreationDate() { return creationDate; }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }
    public double getBalance() { return balance; }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public Client getClient() { return client; }

    public void setClient(Client client) { this.client = client; }

    public Set<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(Set<Transaction> transactions) {
        this.transactions = transactions;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }
    public void addTransaction(Transaction transaction){
        transaction.setAccount(this);
        transactions.add(transaction);
    }
}
