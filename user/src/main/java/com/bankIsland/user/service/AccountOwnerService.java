package com.bankIsland.user.service;

import com.bankIsland.user.dto.AccountOwnerDto;

import java.util.List;

public interface AccountOwnerService {

    public AccountOwnerDto save(AccountOwnerDto accountOwnerDto);

    public AccountOwnerDto findByEmail(String email);

    public boolean existsByEmail(String email);

    public List<AccountOwnerDto> findAll();

    public void deleteById(int id);

    public void delete(AccountOwnerDto accountOwnerDto);

}
