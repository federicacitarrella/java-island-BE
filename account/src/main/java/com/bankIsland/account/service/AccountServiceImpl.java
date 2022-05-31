package com.bankIsland.account.service;

import com.bankIsland.account.dao.AccountRepository;
import com.bankIsland.account.dto.AccountDto;
import com.bankIsland.account.entity.Account;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AccountServiceImpl implements AccountService {

    private AccountRepository accountRepository;

    @Autowired
    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public AccountDto save(AccountDto accountDto) {
        Account account = new Account();
        BeanUtils.copyProperties(accountDto, account);
        accountRepository.save(account);
        accountDto.setId(account.getId());
        return accountDto;
    }

    @Override
    public void delete(AccountDto accountDto) {
        Account account = new Account();
        BeanUtils.copyProperties(accountDto, account);
        accountRepository.delete(account);
    }

    @Override
    public AccountDto findById(int id) {
        Account account = accountRepository.findById(id).get();
        AccountDto accountDto = new AccountDto();
        BeanUtils.copyProperties(account, accountDto);
        return accountDto;
    }

    @Override
    public AccountDto findByAccountNumber(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber).get();
        AccountDto accountDto = new AccountDto();
        BeanUtils.copyProperties(account, accountDto);
        return accountDto;
    }

    @Override
    public List<AccountDto> findAllByAccountOwnerId(int userId) {
        List<Account> accounts = accountRepository.findAllByAccountOwnerId(userId);
        List<AccountDto> accountDtos = new ArrayList<>();
        for (Account a : accounts){
            AccountDto accountDto = new AccountDto();
            BeanUtils.copyProperties(a, accountDto);
            accountDtos.add(accountDto);
        }
        return accountDtos;
    }

    @Override
    public boolean existsByAccountNumber(String accountNumber) {
        return accountRepository.existsByAccountNumber(accountNumber);
    }

    @Override
    public List<AccountDto> findAllByStatus(int status) {
        List<Account> accounts = accountRepository.findAllByStatus(status);
        List<AccountDto> accountDtos = new ArrayList<>();
        for (Account a : accounts){
            AccountDto accountDto = new AccountDto();
            BeanUtils.copyProperties(a, accountDto);
            accountDtos.add(accountDto);
        }
        return accountDtos;
    }

    //    @Override
//    public User findByUsername(String username) {
//        return userRepository.findByUsername(username)
//                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));
//    }
}









