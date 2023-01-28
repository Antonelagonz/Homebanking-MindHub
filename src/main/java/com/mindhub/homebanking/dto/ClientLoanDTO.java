package com.mindhub.homebanking.dto;

import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.ClientLoan;
import com.mindhub.homebanking.models.Loan;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public class ClientLoanDTO {
    private int payments;

    private long id;
    private long loanId;
    private double amount;
    private LocalDateTime date;
    private String nameLoan;

    public ClientLoanDTO(ClientLoan clientLoan){
        this.loanId = clientLoan.getLoan().getId();
        this.amount = clientLoan.getAmount();
        this.payments = clientLoan.getPayments();
        this.date = clientLoan.getDate();
        this.id = clientLoan.getId();
        this.nameLoan = clientLoan.getLoan().getLoanName();
    }


    public int getPayments() {
        return payments;
    }

    public LocalDateTime getDate() {
        return date;
    }


    public long getId() {
        return id;
    }

    public long getLoanId() {
        return loanId;
    }

    public double getAmount() {
        return amount;
    }

    public String getNameLoan() {
        return nameLoan;
    }
}
