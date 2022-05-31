package com.bankIsland.transaction.controller;


import com.bankIsland.transaction.controller.request.TransactionRequest;
import com.bankIsland.transaction.controller.request.TransferRequest;
import com.bankIsland.transaction.controller.response.MessageResponse;
import com.bankIsland.transaction.dto.TransactionDto;
import com.bankIsland.transaction.jwt.JwtUtils;
import com.bankIsland.transaction.rabbit.Sender;
import com.bankIsland.transaction.rabbit.TransactionMessage;
import com.bankIsland.transaction.rabbit.TransferMessage;
import com.bankIsland.transaction.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private Sender sender;
    @Autowired
    private TransactionService transactionService;

    @GetMapping
    public ResponseEntity getTransactions(@RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(transactionService.findAllByAccountOwnerId(jwtUtils.getAccounOwnerIdFromJwtToken(token)));
    }

    @GetMapping("/{accountNumber}")
    public ResponseEntity getTransactions(@RequestHeader("Authorization") String token, @PathVariable String accountNumber) {

        //Add try
        List<TransactionDto> transactions = transactionService.findAllByAccountOwnerIdAndAccountNumberFrom(jwtUtils.getAccounOwnerIdFromJwtToken(token), accountNumber);

        if(transactions.size()==0){
            return ResponseEntity.badRequest().body(new MessageResponse("No operations available for this account."));
        } else if (transactions.size()<10){
            transactions = transactions.subList(0,transactions.size());
        } else {
            transactions = transactions.subList(0,10);
        }
        return ResponseEntity.ok(transactions);
    }

    @PostMapping("/transfer")
    public ResponseEntity transfer(@RequestHeader("Authorization") String token, @RequestBody TransferRequest request) throws IOException {

        TransferMessage message = new TransferMessage(request.getAccountNumberFrom(), request.getAccountNumberTo(), request.getAmount(), request.getCause(), jwtUtils.getAccounOwnerIdFromJwtToken(token));

        Object response = sender.sendTransferRequest(message);

        switch ((int) response) {
            case 1:
                TransactionDto transaction = new TransactionDto(request.getType(), request.getAccountNumberFrom(), request.getAccountNumberTo(),
                        jwtUtils.getAccounOwnerIdFromJwtToken(token), new Date(), request.getCause(), request.getAmount());
                transactionService.save(transaction);

                return ResponseEntity.ok(transaction);
            case 2:
                return ResponseEntity.badRequest().body(new MessageResponse("Invalid amount."));
            case 3:
                return ResponseEntity.badRequest().body(new MessageResponse("Invalid account numbers."));
            case 4:
                return ResponseEntity.badRequest().body(new MessageResponse("Inactive account."));
            default:
                return ResponseEntity.internalServerError().body(new MessageResponse("Server error, try later."));
        }
    }

    @PostMapping
    public ResponseEntity transaction(@RequestHeader("Authorization") String token, @RequestBody TransactionRequest request) throws IOException {

        TransactionMessage message = new TransactionMessage(request.getAccountNumber(), request.getAmount(), jwtUtils.getAccounOwnerIdFromJwtToken(token));

        Object response = sender.sendTransactionRequest(message);

        switch ((int) response) {
            case 1:
                TransactionDto transaction = new TransactionDto(request.getType(), request.getAccountNumber(), null,
                        jwtUtils.getAccounOwnerIdFromJwtToken(token), new Date(), null, request.getAmount());
                transactionService.save(transaction);

                return ResponseEntity.ok(transaction);
            case 2:
                return ResponseEntity.badRequest().body(new MessageResponse("Invalid amount."));
            case 3:
                return ResponseEntity.badRequest().body(new MessageResponse("Invalid account number."));
            case 4:
                return ResponseEntity.badRequest().body(new MessageResponse("Inactive account(s)."));
            default:
                return ResponseEntity.internalServerError().body(new MessageResponse("Server error, try later."));
        }
    }
}
