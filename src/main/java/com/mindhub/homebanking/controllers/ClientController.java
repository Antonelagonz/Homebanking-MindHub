package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dto.ClientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.AccountType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.services.AccountServices;
import com.mindhub.homebanking.services.ClientServices;
import com.mindhub.homebanking.utils.AccountUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;

//se utilizan metodos de rest porque creamos una api rest
//restcontroller dice que se va a regir bajo los lineamientos de rest
//representacion de los datos = json o xml
//estado actual de los datos al momento de ser solicitados
//la transferencia de datos tiene que ser a traves de metodos http: get-post-delete-put-patch
@RestController
@RequestMapping("/api")
public class ClientController {

    @Autowired
    private ClientServices clientServices;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AccountServices accountServices;

    @Autowired
    private AccountRepository accountRepository;

    @RequestMapping("/clients")
    public List<ClientDTO> getClients() {
        return clientServices.getAllClients();
    }

    @RequestMapping("/client/{id}")
    public ClientDTO getClientDTO(@PathVariable Long id) {
        return clientServices.getClientDTO(id);
    }

    @RequestMapping("/client/current")
    public ClientDTO getCurrentClient(Authentication authentication){
        Client client = clientServices.findByEmail(authentication.getName());
        return new ClientDTO(client);
    }

    @PostMapping("/clients")
    public ResponseEntity<Object> register(@RequestParam String firstName,
                                           @RequestParam String lastName,
                                           @RequestParam String email,
                                           @RequestParam String password) {

        if (firstName.isEmpty()) {
            return new ResponseEntity<>("Missing First Name", HttpStatus.FORBIDDEN);
        }else
        if (lastName.isEmpty()){
            return new ResponseEntity<>("Missing Last Name", HttpStatus.FORBIDDEN);
        }else
        if (email.isEmpty()){
            return new ResponseEntity<>("Missing Email", HttpStatus.FORBIDDEN);
        }else
        if (password.isEmpty()){
            return new ResponseEntity<>("Missing Password", HttpStatus.FORBIDDEN);
        }
        if (!email.contains("@") && !email.contains(".com")){
            return new ResponseEntity<>("Mail is not valid", HttpStatus.FORBIDDEN);
        }
        if ((firstName.length()<3) || (lastName.length()<3)){
            return new ResponseEntity<>("Your first name and last name can't be shorter than 3 letters", HttpStatus.FORBIDDEN);
        }
        if (password.length() < 5){
            return new ResponseEntity<>("Your password must have more than 5 chars", HttpStatus.FORBIDDEN);
        }
        if (clientServices.findByEmail(email) !=  null) {
            return new ResponseEntity<>("Mail already in use", HttpStatus.FORBIDDEN);
        }

        Client client = new Client(firstName, lastName, email, passwordEncoder.encode(password));
        clientServices.saveClient(client);
        accountServices.saveAccount(new Account(AccountUtils.getAccountNumber(accountRepository), LocalDateTime.now(),0, client, true, AccountType.CURRENT));
        return new ResponseEntity<>(HttpStatus.CREATED);
    }



}

