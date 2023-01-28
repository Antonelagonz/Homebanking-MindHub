package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dto.LoanDTO;
import com.mindhub.homebanking.models.Loan;

import java.util.List;

public interface LoanServices {

    public List<LoanDTO> getLoansDTO();

    public Loan findById(long id);

    public void saveLoan(Loan loan);
}
