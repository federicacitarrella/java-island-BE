package com.bankIsland.transaction.controller;


import com.bankIsland.transaction.controller.request.TransactionRequest;
import com.bankIsland.transaction.controller.request.TransferRequest;
import com.bankIsland.transaction.controller.response.MessageResponse;
import com.bankIsland.transaction.dto.TransactionDto;
import com.bankIsland.transaction.jwt.JwtUtils;
import com.bankIsland.transaction.rabbit.BackTransferMessage;
import com.bankIsland.transaction.rabbit.Sender;
import com.bankIsland.transaction.rabbit.TransactionMessage;
import com.bankIsland.transaction.rabbit.TransferMessage;
import com.bankIsland.transaction.service.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final JwtUtils jwtUtils;
    private final Sender sender;
    private final TransactionService transactionService;

    public TransactionController(JwtUtils jwtUtils, Sender sender, TransactionService transactionService) {
        this.jwtUtils = jwtUtils;
        this.sender = sender;
        this.transactionService = transactionService;
    }

    @GetMapping
    public ResponseEntity<?> getTransactions(@RequestHeader("Authorization") String token) { //aggiungi alle transactions account_owner_id_to e cerca anche in quello
        return ResponseEntity.ok(transactionService.findAllByAccountOwnerId(jwtUtils.getAccounOwnerIdFromJwtToken(token)));
    }

    @GetMapping("/{accountNumber}")
    public ResponseEntity<?> getTransactions(@RequestHeader("Authorization") String token, @PathVariable String accountNumber) {

        //Add try
        List<TransactionDto> transactions = transactionService.findAllByAccountNumberFromOrAccountNumberTo(accountNumber);

        if (transactions.size() < 10) {
            transactions = transactions.subList(0, transactions.size());
        } else {
            transactions = transactions.subList(0, 10);
        }
        return ResponseEntity.ok(transactions);
    }

    @PostMapping("/transfer")
    public ResponseEntity<?> transfer(@RequestHeader("Authorization") String token, @RequestBody TransferRequest request) throws IOException {

        TransferMessage message = new TransferMessage(request.accountNumberFrom(), request.accountNumberTo(), request.amount(), request.cause(), jwtUtils.getAccounOwnerIdFromJwtToken(token));

        BackTransferMessage response = sender.sendTransferRequest(message);

        switch ((response).status()) {
            case 1:
                TransactionDto transaction = new TransactionDto(request.type(), request.accountNumberFrom(), request.accountNumberTo(),
                        jwtUtils.getAccounOwnerIdFromJwtToken(token), response.accountOwnerIdTo(), Instant.now(), request.cause(), Math.abs(request.amount()));
                transactionService.save(transaction);
//                throw new RuntimeException();
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
    public ResponseEntity<?> transaction(@RequestHeader("Authorization") String token, @RequestBody TransactionRequest request) throws IOException {

        //solo un jwt

        TransactionMessage message = new TransactionMessage(request.accountNumber(), request.amount(), jwtUtils.getAccounOwnerIdFromJwtToken(token));

        Object response = sender.sendTransactionRequest(message);

        switch ((int) response) { //Utility
            case 1:
                TransactionDto transaction = new TransactionDto(request.type(), request.accountNumber(), null,
                        jwtUtils.getAccounOwnerIdFromJwtToken(token), -1, Instant.now(), null, Math.abs(request.amount()));
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
