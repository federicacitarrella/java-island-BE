package com.bankIsland.user.controller;

import com.bankIsland.user.dto.AccountOwnerDto;
import com.bankIsland.user.service.AccountOwnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/account_owners")
public class AccountOwnerController {

    @Autowired
    private AccountOwnerService accountOwnerService;

    @GetMapping("/intern")
    public ResponseEntity registrationList(){
        List<AccountOwnerDto> accountOwnerDtos = accountOwnerService.findAll();
        return ResponseEntity.ok(accountOwnerDtos);
    }
}
