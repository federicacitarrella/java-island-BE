package com.bankIsland.user.service;

import com.bankIsland.user.entity.AccountOwner;

import java.util.List;

public interface AccountOwnerService {

    public AccountOwner save(AccountOwner accountOwner);

    public AccountOwner findByEmail(String email);

    public boolean existsByEmail(String email);

    public List<AccountOwner> findAll();

}
