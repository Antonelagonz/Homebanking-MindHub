package com.mindhub.homebanking.dto;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.AccountType;
import com.mindhub.homebanking.models.Transaction;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class AccountDTO {
    private long id;
    private String number;
    private LocalDateTime creationDate;
    private double balance;
    private Set<TransactionDTO> transactions;

    private Boolean enabled = true;

    private AccountType accountType;


    public AccountDTO(Account account){
        this.id = account.getId();
        this.number = account.getNumber();
        this.creationDate = account.getCreationDate();
        this.balance = account.getBalance();
        this.transactions = account.getTransactions().stream().map(transaction -> new TransactionDTO(transaction)).collect(Collectors.toSet());
        this.enabled = account.getEnabled();
        this.accountType = account.getAccountType();
    }
    //el stream es para utilizar funciones de orden superior como map. por cada account lo convierte en un accountDTO
    //del .stream al .map el dato esta en "string"
    //collectors convierte otra vez al account en un set para que sea compatible con el resto del codigo

    public long getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public Set<TransactionDTO> getTransactions() {
        return transactions;
    }

    public void setTransactions(Set<TransactionDTO> transactions) {
        this.transactions = transactions;
    }


    public Boolean getEnabled(){ return enabled;}

    public AccountType getAccountType() {
        return accountType;
    }
}
