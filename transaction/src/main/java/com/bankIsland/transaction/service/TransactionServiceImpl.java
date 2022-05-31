package com.bankIsland.transaction.service;

import com.bankIsland.transaction.dao.TransactionRepository;
import com.bankIsland.transaction.dto.TransactionDto;
import com.bankIsland.transaction.entity.Transaction;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {

    private TransactionRepository transactionRepository;

    @Autowired
    public TransactionServiceImpl(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public TransactionDto save(TransactionDto transactionDto) {
        Transaction transaction = new Transaction();
        BeanUtils.copyProperties(transactionDto, transaction);
        transactionDto.setId(transactionRepository.save(transaction).getId());
        return transactionDto;
    }

    @Override
    public List<TransactionDto> findAllByAccountOwnerId(int accountOwnerId) {
        List<Transaction> transactions = transactionRepository.findAllByAccountOwnerId(accountOwnerId);
        transactions.sort((Transaction t1, Transaction t2) -> t2.getDate().compareTo(t1.getDate()));
        List<TransactionDto> transactionDtos = new ArrayList<>();
        for(Transaction t : transactions) {
            TransactionDto transactionDto = new TransactionDto();
            BeanUtils.copyProperties(t, transactionDto);
            transactionDtos.add(transactionDto);
        }
        return transactionDtos;
    }

    @Override
    public List<TransactionDto> findAllByAccountOwnerIdAndAccountNumberFrom(int accountOwnerId, String accountNumberFrom) {
        List<Transaction> transactions = transactionRepository.findAllByAccountOwnerIdAndAccountNumberFrom(accountOwnerId, accountNumberFrom);
        transactions.sort((Transaction t1, Transaction t2) -> t2.getDate().compareTo(t1.getDate()));
        List<TransactionDto> transactionDtos = new ArrayList<>();
        for(Transaction t : transactions) {
            TransactionDto transactionDto = new TransactionDto();
            BeanUtils.copyProperties(t, transactionDto);
            transactionDtos.add(transactionDto);
        }
        return transactionDtos;
    }

    @Override
    public TransactionDto findByAccountNumberTo(String accountNumber) {
        Transaction transaction = transactionRepository.findByAccountNumberTo(accountNumber);
        TransactionDto transactionDto = new TransactionDto();
        BeanUtils.copyProperties(transaction, transactionDto);
        return transactionDto;
    }

    //    @Override
//    public User findByUsername(String username) {
//        return userRepository.findByUsername(username)
//                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));
//    }
}









