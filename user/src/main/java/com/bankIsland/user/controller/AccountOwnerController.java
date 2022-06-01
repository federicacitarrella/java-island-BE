package com.bankIsland.user.controller;

import com.bankIsland.user.service.AccountOwnerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/account_owners")
public class AccountOwnerController {

    private final AccountOwnerService accountOwnerService;

    public AccountOwnerController(AccountOwnerService accountOwnerService) {
        this.accountOwnerService = accountOwnerService;
    }

    @GetMapping("/intern")
    public ResponseEntity<?> registrationList(){
        return ResponseEntity.ok(accountOwnerService.findAll());
    }
}
