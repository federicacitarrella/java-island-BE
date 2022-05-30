package com.bankIsland.transaction.service;

import com.bankIsland.transaction.entity.Transaction;

import java.util.List;

public interface TransactionService {

    public Transaction save(Transaction transaction);

    public List<Transaction> findAllByAccountOwnerId(int accountOwnerId);

    List<Transaction> findAllByAccountOwnerIdAndAccountNumberFrom(int accountOwnerId, String accountNumberFrom);

}
