package com.mindhub.homebanking.controllers;

import com.lowagie.text.Document;
import com.lowagie.text.Table;
import com.lowagie.text.pdf.PdfWriter;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.services.AccountServices;
import com.mindhub.homebanking.services.ClientServices;
import com.mindhub.homebanking.services.TransactionServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class TransactionController {

    @Autowired
    private ClientServices clientServices;

    @Autowired
    private AccountServices accountServices;

    @Autowired
    private TransactionServices transactionServices;
    @Transactional
    @PostMapping("/transactions")
    public ResponseEntity<Object> makeTransaction(Authentication authentication,
                                                  @RequestParam String fromAccountNumber,
                                                  @RequestParam String toAccountNumber,
                                                  @RequestParam double amount,
                                                  @RequestParam String description){
        Client client = this.clientServices.findByEmail(authentication.getName());
        Account fromAccount = this.accountServices.findByNumber(fromAccountNumber);
        Account toAccount = this.accountServices.findByNumber(toAccountNumber);
        Set <Account> accountExist = client.getAccounts().stream().filter(account -> account.getNumber().equals(fromAccountNumber)).collect(Collectors.toSet());

        if (amount <= 0) {
            return new ResponseEntity<>("Missing amount", HttpStatus.EXPECTATION_FAILED);
        }
        if (description.isEmpty()) {
            return new ResponseEntity<>("Missing description", HttpStatus.FORBIDDEN);
        }
        if (fromAccountNumber.isEmpty() ) {
            return new ResponseEntity<>("Missing origin Account", HttpStatus.FORBIDDEN);
        }
        if (toAccountNumber.isEmpty()) {
            return new ResponseEntity<>("Missing destiny Account", HttpStatus.FORBIDDEN);
        }

        if (fromAccountNumber.equals(toAccountNumber)){
            return new ResponseEntity<>("Destination account can't be the same as the origin account", HttpStatus.FORBIDDEN);
        }
        if(accountExist.isEmpty()){
            return new ResponseEntity<>("Your origin account doesn't exist", HttpStatus.FORBIDDEN);
        }

        if(toAccount == null){
            return new ResponseEntity<>("Your destiny account doesn't exist", HttpStatus.FORBIDDEN);
        }

        if(fromAccount.getBalance() < amount){
            return new ResponseEntity<>("Not enough balance", HttpStatus.FORBIDDEN);
        }


        Transaction transactionOrigin = new Transaction(TransactionType.DEBIT, -amount, toAccountNumber + " " + description , LocalDateTime.now(), fromAccount);
        Transaction transactionDestination = new Transaction(TransactionType.CREDIT, amount, fromAccountNumber + " " + description , LocalDateTime.now(), toAccount);

        transactionServices.saveTransaction(transactionOrigin);
        transactionServices.saveTransaction(transactionDestination);

        fromAccount.setBalance(fromAccount.getBalance() - amount);
        toAccount.setBalance(toAccount.getBalance() + amount);

        accountServices.saveAccount(fromAccount);
        accountServices.saveAccount(toAccount);

        return new ResponseEntity<>(HttpStatus.CREATED);

    }

    private ByteArrayInputStream createPdf(Set<Transaction> transactions) throws IOException {
        // Creating a PDF document
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, out);
        document.open();

        // Creating a table
        Table table = new Table(4);
        table.setWidth(100);
        table.setBorderWidth(1);
        table.setPadding(5);

        // Adding cells to the table
        table.addCell("Type");
        table.addCell("Amount");
        table.addCell("Description");
        table.addCell("Date");
        transactions.forEach(transaction -> {
            table.addCell(transaction.getType().toString());
            table.addCell(String.valueOf(transaction.getAmount()));
            table.addCell(transaction.getDescription());
            table.addCell(transaction.getDate().toString());
        });

        document.add(table);

        // Closing the document
        document.close();

        return new ByteArrayInputStream(out.toByteArray());
    }

    @PostMapping(value = "/downloadPDF", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> downloadPdf(@RequestBody Set<Transaction> transactions) throws IOException {
        ByteArrayInputStream pdf = createPdf(transactions); // call the method that creates the pdf

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=table.pdf");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(pdf));
    }

}
