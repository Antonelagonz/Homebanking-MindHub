package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dto.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.AccountType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.services.AccountServices;
import com.mindhub.homebanking.services.ClientServices;
import com.mindhub.homebanking.services.TransactionServices;
import com.mindhub.homebanking.utils.AccountUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class AccountController {

    @Autowired
    private AccountServices accountServices;

    @Autowired
    private ClientServices clientServices;

    @Autowired
    private TransactionServices transactionServices;

    @Autowired
    private AccountRepository accountRepository;

    @RequestMapping("/accounts")
    public List<AccountDTO> getAllAccounts(){
        return accountServices.getAllAccounts();
    }

    @RequestMapping("/accounts/{id}")
    public AccountDTO getAccount(@PathVariable Long id){
        return accountServices.getAccount(id);
    }

    @RequestMapping("/client/current/accounts")
    public AccountDTO getAccount(Authentication authentication){
        Client client = this.clientServices.findByEmail(authentication.getName());
        return new AccountDTO(accountServices.findByNumber(authentication.getName()));
    }

    @PostMapping("/client/current/accounts")
    public ResponseEntity<Object> register(Authentication authentication, @RequestParam AccountType accountType) {
        Client client = this.clientServices.findByEmail(authentication.getName());
        Set<Account> accountActive = client.getAccounts().stream().filter(account -> account.getEnabled() == true).collect(Collectors.toSet());

        if (accountActive.size() >= 3){
            return new ResponseEntity<>("You have reached the max limit of accounts.", HttpStatus.FORBIDDEN);
        }
        accountServices.saveAccount(new Account(AccountUtils.getAccountNumber(accountRepository), LocalDateTime.now(),0, client, true, accountType));
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Transactional
    @PatchMapping("/client/current/accounts/disable")
    public ResponseEntity<Object> disableAccount(@RequestParam String number, Authentication authentication){
     Client client = clientServices.findByEmail(authentication.getName());
     Account account = accountServices.findByNumber(number);

     if (number.isEmpty()){
         return new ResponseEntity<>("Missing account", HttpStatus.FORBIDDEN);
     }

     if (account.getEnabled() != true){
         return new ResponseEntity<>("The account is already disabled", HttpStatus.FORBIDDEN);
     }
     if (account.getBalance() > 0){
         return new ResponseEntity<>("You can't disable an account with balance available. In order to disable this account make sure the balance is 0", HttpStatus.FORBIDDEN);
     }
        account.setEnabled(false);
        transactionServices.deleteTransactions();
        accountServices.saveAccount(account);
        return new ResponseEntity<>("Your account was disabled successfully", HttpStatus.ACCEPTED);
    }

}