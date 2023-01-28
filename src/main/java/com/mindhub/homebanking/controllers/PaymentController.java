package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dto.PaymentDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.services.AccountServices;
import com.mindhub.homebanking.services.CardServices;
import com.mindhub.homebanking.services.PaymentServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@RestController
@RequestMapping("/api")
public class PaymentController {

    @Autowired
    CardServices cardServices;
    @Autowired
    AccountServices accountServices;
    @Autowired
    PaymentServices paymentServices;

    @GetMapping("/getPayments")
    public List<PaymentDTO> getPayments() {
        List<Payment> payments = paymentServices.getPayments();
        return paymentServices.getPaymentsDTO(paymentServices.getPayments());
    }

    @Transactional
    @PostMapping("/payments")
    public ResponseEntity<Object> receivePayment(@RequestParam String cardNumber, @RequestParam Integer cvv,
                                                 @RequestParam Double amount, @RequestParam String description) {
        Card card = cardServices.findByCardNumber(cardNumber);
        Account account = card.getAccount();

        if(card == null)
            return new ResponseEntity<>("Missing card information", HttpStatus.FORBIDDEN);
        if (!Objects.equals(card.getCvv(), cvv))
            return new ResponseEntity<>("Wrong card information", HttpStatus.FORBIDDEN);

        if(account != null) {
            if (account.getBalance() < amount) {
                return new ResponseEntity<>("You don't have enough balance in your account " + account.getNumber(), HttpStatus.FORBIDDEN);
            }
            Payment payment = new Payment(cardNumber, amount, description, cvv);
            payment.setCard(card);
            paymentServices.savePayment(payment);
            card.addPayment(payment);
            cardServices.saveCard(card);
            Transaction transaction = new Transaction(TransactionType.DEBIT, amount, description, LocalDateTime.now(), account);
            Set<Transaction> transactions = account.getTransactions();
            transactions.add(transaction);
            account.setTransactions(transactions);
            account.setBalance(account.getBalance() - amount);
            accountServices.saveAccount(account);
            return new ResponseEntity<>("The payment has been sent", HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity<>("The payment has been sent" + account.toString(), HttpStatus.ACCEPTED);
        }

    }
}
