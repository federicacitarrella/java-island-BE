package com.bankIsland.transaction.controller;


import com.bankIsland.transaction.controller.request.TransactionRequest;
import com.bankIsland.transaction.controller.request.TransferRequest;
import com.bankIsland.transaction.controller.response.MessageResponse;
import com.bankIsland.transaction.entity.Transaction;
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

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private Sender sender;
    @Autowired
    private TransactionService transactionService;

    @PostMapping("/transfer")
    public ResponseEntity transfer(@RequestHeader("Authorization") String token, @RequestBody TransferRequest request) throws IOException {

        TransferMessage message = new TransferMessage(request.getAccountNumberFrom(), request.getAccountNumberTo(), request.getAmount(), request.getCause(), jwtUtils.getAccounOwnerIdFromJwtToken(token));

        Object response = sender.sendTransferRequest(message);

        switch ((int) response) {
            case 1:
                Transaction transaction = new Transaction(1, request.getAccountNumberFrom(), request.getAccountNumberTo(),
                        new Date(), request.getCause(), request.getAmount());
                transactionService.save(transaction);

                return ResponseEntity.ok(transaction);
            case 2:
                return ResponseEntity.badRequest().body(new MessageResponse("Invalid amount."));
            case 3:
                return ResponseEntity.badRequest().body(new MessageResponse("Invalid account numbers."));
            default:
                return ResponseEntity.internalServerError().body(new MessageResponse("Server error, try later."));
        }
    }

    @PostMapping
    public ResponseEntity deposit(@RequestHeader("Authorization") String token, @RequestBody TransactionRequest request) throws IOException {

        TransactionMessage message = new TransactionMessage(request.getAccountNumber(), request.getAmount(), jwtUtils.getAccounOwnerIdFromJwtToken(token));

        Object response = sender.sendTransactionRequest(message);

        int type = (request.getAmount()>0)?2:3;

        switch ((int) response) {
            case 1:
                Transaction transaction = new Transaction(type, request.getAccountNumber(), null,
                        new Date(), null, request.getAmount());
                transactionService.save(transaction);

                return ResponseEntity.ok(transaction);
            case 2:
                return ResponseEntity.badRequest().body(new MessageResponse("Invalid amount."));
            case 3:
                return ResponseEntity.badRequest().body(new MessageResponse("Invalid account number."));
            default:
                return ResponseEntity.internalServerError().body(new MessageResponse("Server error, try later."));
        }
    }
}
