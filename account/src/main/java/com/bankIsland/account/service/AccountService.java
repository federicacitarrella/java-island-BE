package com.bankIsland.account.service;

import com.bankIsland.account.controller.request.AccountCreationRequest;
import com.bankIsland.account.dto.AccountDto;

import java.util.List;

public interface AccountService {

    AccountDto save(AccountDto accountDto);

    boolean delete(AccountDto accountDto);

    AccountDto findById(int id);

    AccountDto findByAccountNumber(String accountNumber);

    List<AccountDto> findAllByAccountOwnerId(int userId);

    List<AccountDto> findAllByStatus(int status);

    boolean existsByAccountNumber(String accountNumber);

    AccountDto createAccount(AccountDto accountDto, AccountCreationRequest request);

    AccountDto createFirstAccount(int accountOwnerId, AccountCreationRequest request);

    boolean closeAccount(AccountDto accountDto);

    boolean isFirstAccount(int accountOwnerId);

    boolean isWaitingAccount(int accountOwnerId);

//    boolean isInactive(int accountOwnerId);

    boolean validateAccount(int accountId);

    boolean rejectAccount(AccountDto accountDto);

    boolean rejectNewAccount(AccountDto accountFrom, AccountDto accountTo);

    boolean rejectFirstAccount(AccountDto accountDto);

}
