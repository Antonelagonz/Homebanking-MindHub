package com.mindhub.homebanking;

import com.mindhub.homebanking.utils.AccountUtils;
import com.mindhub.homebanking.repositories.AccountRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AccountUtilsTest {

    @Autowired
    AccountRepository accountRepository;

    /*-------------------ACCOUNT UTIL TEST-----------------*/

    @Test
    public void accountNumberIsCreated(){
        String accountNumber = AccountUtils.getAccountNumber(accountRepository);
        assertThat(accountNumber,is(not(emptyOrNullString())));
    }
}

