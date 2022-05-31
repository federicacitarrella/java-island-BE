package com.bankIsland.user.service;

import com.bankIsland.user.dao.AccountOwnerRepository;
import com.bankIsland.user.dto.AccountOwnerDto;
import com.bankIsland.user.entity.AccountOwner;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AccountOwnerServiceImpl implements AccountOwnerService {

    private AccountOwnerRepository accountOwnerRepository;

    @Autowired
    public AccountOwnerServiceImpl(AccountOwnerRepository accountOwnerRepository) {
        this.accountOwnerRepository = accountOwnerRepository;
    }

    @Override
    public AccountOwnerDto save(AccountOwnerDto accountOwnerDto) {
        AccountOwner accountOwner = new AccountOwner();
        BeanUtils.copyProperties(accountOwnerDto, accountOwner);
        accountOwnerRepository.save(accountOwner);
        accountOwnerDto.setId(accountOwner.getId());
        return accountOwnerDto;
    }

    @Override
    public AccountOwnerDto findByEmail(String email) {
        AccountOwner accountOwner = accountOwnerRepository.findByEmail(email).get();
        AccountOwnerDto accountOwnerDto = new AccountOwnerDto();
        BeanUtils.copyProperties(accountOwner, accountOwnerDto);
        return accountOwnerDto;
    }

    @Override
    public boolean existsByEmail(String email) {
        return accountOwnerRepository.existsByEmail(email);
    }

    @Override
    public List<AccountOwnerDto> findAll() {
        List<AccountOwner> accountOwners = accountOwnerRepository.findAll(Sort.by(Sort.Direction.ASC, "firstName").and(Sort.by("lastName")));
        List<AccountOwnerDto> accountOwnerDtos = new ArrayList<>();
        for (AccountOwner a : accountOwners){
            AccountOwnerDto accountOwnerDto = new AccountOwnerDto();
            BeanUtils.copyProperties(a, accountOwnerDto);
            accountOwnerDtos.add(accountOwnerDto);
        }
        return accountOwnerDtos;
    }

    @Override
    public void deleteById(int id) {
        accountOwnerRepository.deleteById(id);
    }

    @Override
    public void delete(AccountOwnerDto accountOwnerDto) {
        AccountOwner accountOwner = new AccountOwner();
        BeanUtils.copyProperties(accountOwnerDto, accountOwner);
        accountOwnerRepository.delete(accountOwner);
    }
}









