package com.bankIsland.transaction.service;

import com.bankIsland.transaction.dto.TransactionDto;

import java.util.List;

public interface TransactionService {

    public TransactionDto save(TransactionDto transactionDto);

    public List<TransactionDto> findAllByAccountOwnerId(int accountOwnerId);

    List<TransactionDto> findAllByAccountOwnerIdAndAccountNumberFrom(int accountOwnerId, String accountNumberFrom);

}
