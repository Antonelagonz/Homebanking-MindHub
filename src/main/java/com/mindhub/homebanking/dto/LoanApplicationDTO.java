package com.mindhub.homebanking.dto;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.ClientLoan;
import com.mindhub.homebanking.models.Loan;

public class LoanApplicationDTO {

    private long loanId;

    private double amount;

    private int payments;

    private String toAccountNumber;

    public LoanApplicationDTO(){

    }

    public LoanApplicationDTO(ClientLoan clientLoan, Account account){
        this.loanId = clientLoan.getLoan().getId();
        this.amount = clientLoan.getAmount();
        this.payments = clientLoan.getPayments();
        this.toAccountNumber = account.getNumber();
    }

    public long getLoanId() {
        return loanId;
    }

    public double getAmount() {
        return amount;
    }

    public int getPayments() {
        return payments;
    }

    public String getToAccountNumber() {
        return toAccountNumber;
    }
}
