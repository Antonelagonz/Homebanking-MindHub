package com.mindhub.homebanking.services;

import com.mindhub.homebanking.models.Transaction;

public interface TransactionServices {

    void saveTransaction(Transaction transaction);

    void deleteTransactions();
}
