package com.bankIsland.account.service;

import com.bankIsland.account.controller.request.AccountCreationRequest;
import com.bankIsland.account.dao.AccountRepository;
import com.bankIsland.account.dto.AccountDto;
import com.bankIsland.account.entity.Account;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
    public boolean delete(AccountDto accountDto) {
        Account account = new Account();
        BeanUtils.copyProperties(accountDto, account);
        if (account.getStatus()==4){
            accountRepository.delete(account);
            return true;
        } else {
            return false;
        }

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

    @Override
    public AccountDto createAccount(AccountDto accountDto, AccountCreationRequest request) {
        String accountNumber;
        do {
            accountNumber = "IT" + new Random().nextInt(1000000000);
        } while (existsByAccountNumber(accountNumber));
        accountDto.setBalance(accountDto.getBalance() - request.amount());
        save(accountDto);
        return save(new AccountDto(accountNumber, request.firstName(), request.lastName(), request.amount(), 2, accountDto.getAccountOwnerId()));
    }

    @Override
    public AccountDto createFirstAccount(int accountOwnerId, AccountCreationRequest request) {
        String accountNumber;
        do {
            accountNumber = "IT" + new Random().nextInt(1000000000);
        } while (existsByAccountNumber(accountNumber));
        return save(new AccountDto(accountNumber, request.firstName(), request.lastName(), request.amount(), 2, accountOwnerId));
    }

    @Override
    public boolean closeAccount(AccountDto accountDto) {
        try {
            if(accountDto.getBalance() == 0) {
                accountDto.setStatus(3);
                save(accountDto);
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean validateAccount(int accountId) {
        AccountDto account = findById(accountId);
        if (account.getStatus()==1 || account.getStatus()==2 || account.getStatus()==3) {
            account.setStatus(0);
            save(account);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean rejectAccount(AccountDto accountDto) {
        try {
            accountDto.setStatus(4);
            save(accountDto);
            return true;
        } catch (Exception e){
            return false;
        }
    }

    @Override
    public boolean rejectNewAccount(AccountDto accountFrom, AccountDto accountTo) {
        try {
            accountTo.setBalance(accountTo.getBalance() + accountFrom.getBalance());
            accountFrom.setBalance(0);
            accountFrom.setStatus(4);
            save(accountFrom);
            save(accountTo);
            return true;
        } catch (Exception e) {
            return false;
        }

    }

    @Override
    public boolean isFirstAccount(int accountOwnerId) {
        List<Account> accounts = accountRepository.findAllByAccountOwnerId(accountOwnerId);
        for (Account account : accounts){
            if (account.getStatus()==0 || account.getStatus()==1 || account.getStatus()==2 || account.getStatus()==3 || account.getStatus()==5) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean isWaitingAccount(int accountOwnerId) {
        List<Account> accounts = accountRepository.findAllByAccountOwnerId(accountOwnerId);
        for (Account account : accounts){
            if (account.getStatus()==1 || account.getStatus()==2 || account.getStatus()==3) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean rejectFirstAccount(AccountDto accountDto) {
        accountDto.setStatus(5);
        save(accountDto);
        return true;
    }

    //    @Override
//    public User findByUsername(String username) {
//        return userRepository.findByUsername(username)
//                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));
//    }
}









