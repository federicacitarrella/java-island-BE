package com.bankIsland.transaction.service;

import com.bankIsland.transaction.dto.TransactionDto;

import java.util.List;

public interface TransactionService {

    TransactionDto save(TransactionDto transactionDto);

    List<TransactionDto> findAllByAccountOwnerId(int accountOwnerId);

    List<TransactionDto> findAllByAccountNumberFromOrAccountNumberTo(String accountNumber);

    TransactionDto findByAccountNumberTo(String accountNumber);

}
