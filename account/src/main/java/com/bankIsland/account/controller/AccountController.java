package com.bankIsland.account.controller;

import com.bankIsland.account.controller.request.AccountCreationRequest;
import com.bankIsland.account.controller.response.MessageResponse;
import com.bankIsland.account.dto.AccountDto;
import com.bankIsland.account.exception.ApiBankException;
import com.bankIsland.account.rabbit.ClosingAccountMessage;
import com.bankIsland.account.rabbit.OpeningTransactionMessage;
import com.bankIsland.account.rabbit.Sender;
import com.bankIsland.account.security.jwt.JwtUtils;
import com.bankIsland.account.service.AccountService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private AccountService accountService;

    @Autowired
    private Sender sender;

    @PostMapping
    @Transactional(rollbackFor = ApiBankException.class)
    public ResponseEntity createAccount(@RequestHeader("Authorization") String token, @RequestBody AccountCreationRequest request) throws ApiBankException {

        int accountOwnerId = jwtUtils.getAccounOwnerIdFromJwtToken(token);
        Random rand = new Random();
        try {
            AccountDto account = accountService.findByAccountNumber(request.getAccountNumber());
            if (accountOwnerId == account.getAccountOwnerId()) {
                if (request.getAmount() <= account.getBalance() && request.getAmount() > 0) {
                    String accountNumber;
                    do {
                        accountNumber = "IT" + rand.nextInt(1000000000);
                    } while (accountService.existsByAccountNumber(accountNumber));
                    OpeningTransactionMessage message = new OpeningTransactionMessage(accountOwnerId, account.getAccountNumber(), accountNumber,request.getAmount());
                    account.setBalance(account.getBalance() - request.getAmount());
                    accountService.save(account);
                    AccountDto newAccount = accountService.save(new AccountDto(accountNumber, request.getFirstName(), request.getLastName(), request.getAmount(), 2, accountOwnerId));
                    if (sender.openingTransactionRequest(message)){
                        return ResponseEntity.ok(newAccount);
                    } else {
                        throw new ApiBankException("Server error, try later.");
                    }
                } else {
                    return ResponseEntity.badRequest().body(new MessageResponse("Error: Invalid amount."));
                }
            }
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Invalid request."));
        } catch (NoSuchElementException e) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Invalid accountNumber."));
        } catch (JsonProcessingException e) {
            return ResponseEntity.internalServerError().body(new MessageResponse("Server error, try later."));
//        } catch (ApiBankException e) {
//            return ResponseEntity.internalServerError().body(new MessageResponse("Server error, try later."));
        }
    }

    @PutMapping("/{account_id}")
    public ResponseEntity closureAccount(@RequestHeader("Authorization") String token, @PathVariable int account_id) {

        int accountOwnerId = jwtUtils.getAccounOwnerIdFromJwtToken(token);

        try {
            AccountDto account = accountService.findById(account_id);
            if (accountOwnerId == account.getAccountOwnerId()) {
                if (account.getBalance() == 0) {
                    account.setStatus(3);
                    accountService.save(account);
                    return ResponseEntity.ok(new MessageResponse("Request correctly sent."));
                } else {
                    return ResponseEntity.badRequest().body(new MessageResponse("Error: the card amount should be 0."));
                }
            }
        } catch (NoSuchElementException e) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Invalid accountNumber."));
        }

        return ResponseEntity.badRequest().body(new MessageResponse("Error: Invalid request."));
    }

    @GetMapping
    public ResponseEntity getAccounts(@RequestHeader("Authorization") String token){
        try {
            return ResponseEntity.ok(accountService.findAllByAccountOwnerId(jwtUtils.getAccounOwnerIdFromJwtToken(token)));
        } catch (NoSuchElementException e){
            return ResponseEntity.badRequest().body(new MessageResponse("No accounts available."));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new MessageResponse("Server error, try later."));
        }
    }

    @GetMapping("/intern/registrations")
    public ResponseEntity registrationList(){
        List<AccountDto> accounts = accountService.findAllByStatus(1);
        return ResponseEntity.ok(accounts);
    }

    @GetMapping("/intern/opening_accounts")
    public ResponseEntity openingList(){
        List<AccountDto> accounts = accountService.findAllByStatus(2);
        return ResponseEntity.ok(accounts);
    }

    @GetMapping("/intern/closing_accounts")
    public ResponseEntity closingList(){
        List<AccountDto> accounts = accountService.findAllByStatus(3);
        return ResponseEntity.ok(accounts);
    }

    @PutMapping("/intern/validation/{account_id}")
    public ResponseEntity validateAccount(@PathVariable int account_id){
        try {
            AccountDto account = accountService.findById(account_id);
            account.setStatus(0);
            accountService.save(account); //metodo service
            return ResponseEntity.ok(new MessageResponse("Success"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error"));
        }
    }

    @PutMapping("/intern/rejection/{account_id}")
    public ResponseEntity rejectAccount(@PathVariable int account_id){
        try {
            AccountDto account = accountService.findById(account_id);
            if(account.getStatus()==3){
                account.setStatus(4);
                accountService.save(account);
                return ResponseEntity.ok(new MessageResponse("Success"));
            } else if(account.getStatus()==2) {
                account.setStatus(4);
                ClosingAccountMessage message = new ClosingAccountMessage(account.getAccountNumber());
                String accountNumber = sender.closingAccountRequest(message);
                if (accountNumber != null) {
                    AccountDto accountBack = accountService.findByAccountNumber(accountNumber);
                    accountBack.setBalance(accountBack.getBalance() + account.getBalance());
                    account.setBalance(0);
                    accountService.save(account);
                    accountService.save(accountBack);
                    return ResponseEntity.ok(new MessageResponse("Success"));
                } else {
                    return ResponseEntity.internalServerError().body(new MessageResponse("Server error, try later."));
                }
            }
            return ResponseEntity.badRequest().body(new MessageResponse("Action not available for this account."));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error"));
        }
    }

//    @PutMapping("/intern/closing_validation/")
//    public ResponseEntity closingValidationAccount(@RequestBody AccountClosingRequest closingRequest){
//        try {
//            AccountDto closingAccount = accountService.findByAccountNumber(closingRequest.getAccountNumberClosing());
//            AccountDto accountTo = accountService.findByAccountNumber(closingRequest.getAccountNumberTo());
//
//            accountTo.setBalance(accountTo.getBalance() + closingAccount.getBalance());
//
//            closingAccount.setStatus(4);
//            closingAccount.setBalance(0);
//
//            accountService.save(closingAccount);
//            accountService.save(accountTo);
//
//            return ResponseEntity.ok(new MessageResponse("Success"));
//
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body(new MessageResponse("Error"));
//        }
//    }

    @DeleteMapping("/intern/delete/{account_id}")
    public ResponseEntity deleteAccount(@PathVariable int account_id){
        try {
            AccountDto account = accountService.findById(account_id);
            accountService.delete(account);
            return ResponseEntity.ok(new MessageResponse("Success"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error"));
        }
    }
}
