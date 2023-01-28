package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dto.LoanApplicationDTO;
import com.mindhub.homebanking.dto.LoanDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.services.*;
import net.bytebuddy.asm.Advice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.net.Authenticator;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;


@RestController
@RequestMapping("/api")
public class LoanController {

    @Autowired
    private AccountServices accountServices;

    @Autowired
    private ClientServices clientServices;

    @Autowired
    private ClientLoanServices clientLoanServices;

    @Autowired
    private TransactionServices transactionServices;

    @Autowired
    private LoanServices loanServices;

    @GetMapping("/loans")
    public List<LoanDTO> getLoansDTO(){
        return loanServices.getLoansDTO();
    }


    @Transactional
    @PostMapping("/loans")
    public ResponseEntity<Object> applyLoan(Authentication authentication,
                                            @RequestBody LoanApplicationDTO loanApplicationDTO) {
        Client client = this.clientServices.findByEmail(authentication.getName());
        Loan loan = this.loanServices.findById(loanApplicationDTO.getLoanId());
        Account toAccount = this.accountServices.findByNumber(loanApplicationDTO.getToAccountNumber());


        if (client == null) {
            return new ResponseEntity<>("Client wasn't found", HttpStatus.FORBIDDEN);
        }
        if (loanApplicationDTO.getAmount() <= 0) {
            return new ResponseEntity<>("Wrong or empty Amount", HttpStatus.BAD_REQUEST);
        }
        if (loanApplicationDTO.getPayments() <= 0) {
            return new ResponseEntity<>("Missing Payments", HttpStatus.BAD_REQUEST);
        }
        if (loan == null) {
            return new ResponseEntity<>("Loan Doesn't Exist", HttpStatus.BAD_REQUEST);
        }
        if (loanApplicationDTO.getAmount() < 20000){
            return new ResponseEntity<>("The minimum amount for a loan is $20000", HttpStatus.BAD_REQUEST);
        }
        if (loanApplicationDTO.getAmount() > loan.getMaxAmount()) {
            return new ResponseEntity<>("Amount Greater than Max Amount", HttpStatus.BAD_REQUEST);
        }
        if (toAccount == null) {
            return new ResponseEntity<>("Destiny Account Doesn't Exist", HttpStatus.BAD_REQUEST);
        }
        if (client.getClientLoans().stream().filter(clientLoan -> clientLoan.getLoan().getLoanName().equals(loan.getLoanName())).toArray().length >= 1) {
            return new ResponseEntity<>("Client already has loan of this type", HttpStatus.FORBIDDEN);
        }
        if (!client.getAccounts().contains(toAccount)) {
            return new ResponseEntity<>("Client does not contain account selected", HttpStatus.BAD_REQUEST);
        }
        if (!loan.getPayments().contains(loanApplicationDTO.getPayments())) {
            return new ResponseEntity<>("The payments you select exceeds the max amount of payments", HttpStatus.FORBIDDEN);
        }
        if (client.getClientLoans().size() >= 3) {
            return new ResponseEntity<>("Client has reached the max limit of loans", HttpStatus.FORBIDDEN);
        }
    //11 morgate loan, 12 personal loan, 13 car loan
        if (loanApplicationDTO.getLoanId() == 11){
            ClientLoan clientLoan = new ClientLoan(loanApplicationDTO.getAmount()*1.45, loanApplicationDTO.getPayments(), LocalDateTime.now(), client, loan);
            clientLoanServices.saveClientLoan(clientLoan);
        }

        if (loanApplicationDTO.getLoanId() == 12){
            ClientLoan clientLoan = new ClientLoan(loanApplicationDTO.getAmount()*1.20, loanApplicationDTO.getPayments(), LocalDateTime.now(), client, loan);
            clientLoanServices.saveClientLoan(clientLoan);
        }

        if (loanApplicationDTO.getLoanId() == 13){
            ClientLoan clientLoan = new ClientLoan(loanApplicationDTO.getAmount()*1.30, loanApplicationDTO.getPayments(), LocalDateTime.now(), client, loan);
            clientLoanServices.saveClientLoan(clientLoan);
        }

        String description = "- Solicitude approved";
        Transaction transaction = new Transaction(TransactionType.CREDIT, loanApplicationDTO.getAmount(), loan.getLoanName() + " " + description, LocalDateTime.now(), toAccount);
        toAccount.addTransaction(transaction);
        transactionServices.saveTransaction(transaction);

        toAccount.setBalance(toAccount.getBalance() + loanApplicationDTO.getAmount());
        accountServices.saveAccount(toAccount);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Transactional
    @PostMapping("/loans/create")
    public ResponseEntity<Object> createLoan(@RequestParam double maxAmount,
                                             @RequestParam List<Integer> payments,
                                             @RequestParam String loanName) {

        if(loanName.isEmpty()) {
            return new ResponseEntity<>("The loan name input is empty.", HttpStatus.FORBIDDEN);
        }
        if(maxAmount <= 20000) {
            return new ResponseEntity<>("The min amount is 20000.", HttpStatus.FORBIDDEN);
        }
        if(payments.stream().anyMatch(payment -> payment <= 0)) {
            return new ResponseEntity<>("One or more payments are 0", HttpStatus.FORBIDDEN);
        }
        Loan newLoan = new Loan(maxAmount, payments, loanName);
        loanServices.saveLoan(newLoan);
        return new ResponseEntity<>("The loan was created successfully.", HttpStatus.CREATED);
    }

}
