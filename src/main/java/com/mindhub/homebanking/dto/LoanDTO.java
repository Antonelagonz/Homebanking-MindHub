package com.mindhub.homebanking.dto;

import com.mindhub.homebanking.models.Loan;

import java.util.List;

public class LoanDTO {
    private List<Integer> payments;
    private long id;
    private double maxAmount;
    private String loanName;

    public LoanDTO(Loan loan){
        this.id = loan.getId();
        this.maxAmount = loan.getMaxAmount();
        this.loanName = loan.getLoanName();
        this.payments = loan.getPayments();
    }

    public long getId() { return id; }

    public List<Integer> getPayments() { return payments; }

    public double getMaxAmount() { return maxAmount; }

    public String getLoanName() { return loanName; }

}
