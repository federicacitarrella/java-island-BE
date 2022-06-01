package com.bankIsland.user.service;

import com.bankIsland.user.controller.payload.request.SignupRequest;
import com.bankIsland.user.dto.AccountOwnerDto;

import java.util.List;

public interface AccountOwnerService {

    public AccountOwnerDto save(AccountOwnerDto accountOwnerDto);

    public AccountOwnerDto findByEmail(String email);

    public boolean existsByEmail(String email);

    public List<AccountOwnerDto> findAll();

    public AccountOwnerDto registration(SignupRequest signupRequest);

}
