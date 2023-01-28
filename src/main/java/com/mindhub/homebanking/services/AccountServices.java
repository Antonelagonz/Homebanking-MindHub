package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dto.AccountDTO;
import com.mindhub.homebanking.models.Account;

import java.util.List;

public interface AccountServices {

     List<AccountDTO> getAllAccounts();

     AccountDTO getAccount(Long id);

     void saveAccount(Account account);

     Account findByNumber(String number);
}
