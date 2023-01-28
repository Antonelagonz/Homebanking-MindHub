package com.mindhub.homebanking.services.implementation;


import com.mindhub.homebanking.dto.LoanDTO;
import com.mindhub.homebanking.models.Loan;
import com.mindhub.homebanking.repositories.LoanRepository;
import com.mindhub.homebanking.services.LoanServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class LoanServicesImplementation implements LoanServices {

    @Autowired
    LoanRepository loanRepository;

    @Override
    public List<LoanDTO> getLoansDTO() {
        return loanRepository.findAll().stream().map(loan -> new LoanDTO(loan)).collect(toList());
    }

    @Override
    public Loan findById(long id) {
        return loanRepository.findById(id).orElse(null);
    }

    @Override
    public void saveLoan(Loan loan) {
    loanRepository.save(loan);
    }
}
