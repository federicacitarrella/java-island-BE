package com.bankIsland.transaction.service;

import com.bankIsland.transaction.dao.TransactionRepository;
import com.bankIsland.transaction.entity.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {

    private TransactionRepository transactionRepository;

    @Autowired
    public TransactionServiceImpl(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public Transaction save(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    @Override
    public List<Transaction> findAllByAccountOwnerId(int accountOwnerId) {
        List<Transaction> transactions = transactionRepository.findAllByAccountOwnerId(accountOwnerId);
        transactions.sort((Transaction t1, Transaction t2) -> t2.getDate().compareTo(t1.getDate()));
        return transactions;
    }

    @Override
    public List<Transaction> findAllByAccountOwnerIdAndAccountNumberFrom(int accountOwnerId, String accountNumberFrom) {
        List<Transaction> transactions = transactionRepository.findAllByAccountOwnerIdAndAccountNumberFrom(accountOwnerId, accountNumberFrom);
        transactions.sort((Transaction t1, Transaction t2) -> t2.getDate().compareTo(t1.getDate()));
        return transactions;
    }

    //    @Override
//    public User findByUsername(String username) {
//        return userRepository.findByUsername(username)
//                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));
//    }
}









