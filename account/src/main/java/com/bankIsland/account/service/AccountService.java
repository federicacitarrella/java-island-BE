package com.bankIsland.account.service;

import com.bankIsland.account.dto.AccountDto;

import java.util.List;

public interface AccountService {

    public AccountDto save(AccountDto accountDto);

    public void delete(AccountDto accountDto);

    public AccountDto findById(int id);

    public AccountDto findByAccountNumber(String accountNumber);

    public List<AccountDto> findAllByAccountOwnerId(int userId);

    public List<AccountDto> findAllByStatus(int status);

    public boolean existsByAccountNumber(String accountNumber);



}
