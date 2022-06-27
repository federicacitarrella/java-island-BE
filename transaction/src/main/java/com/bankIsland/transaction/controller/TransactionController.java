package com.bankIsland.transaction.controller;


import com.bankIsland.transaction.controller.request.TransactionRequest;
import com.bankIsland.transaction.controller.request.TransferRequest;
import com.bankIsland.transaction.controller.response.MessageResponse;
import com.bankIsland.transaction.dto.TransactionDto;
import com.bankIsland.transaction.jwt.JwtUtils;
import com.bankIsland.transaction.rabbit.Sender;
import com.bankIsland.transaction.rabbit.message.BackTransferMessage;
import com.bankIsland.transaction.rabbit.message.TransactionMessage;
import com.bankIsland.transaction.rabbit.message.TransferMessage;
import com.bankIsland.transaction.service.TransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(TransactionController.class);

    public TransactionController(JwtUtils jwtUtils, Sender sender, TransactionService transactionService) {
        this.jwtUtils = jwtUtils;
        this.sender = sender;
        this.transactionService = transactionService;
    }

    @GetMapping
    public ResponseEntity<?> getTransactions(@RequestHeader("Authorization") String token) {
        logger.info(">>> Request list of all transactions");
        return ResponseEntity.ok(transactionService.findAllByAccountOwnerId(jwtUtils.getAccountOwnerIdFromJwtToken(token)));
    }

    @GetMapping("/{accountNumber}/{transactionsQuantity}")
    public ResponseEntity<?> getTransactions(@PathVariable String accountNumber, @PathVariable int transactionsQuantity) {

        logger.info(">>> Request list transactions account " + accountNumber);

        try {
            List<TransactionDto> transactions = transactionService.findAllByAccountNumberFromOrAccountNumberTo(accountNumber);

            if ((transactionsQuantity != -1) && (transactions.size() > transactionsQuantity)) {
                transactions = transactions.subList(0, transactionsQuantity);
            }
            return ResponseEntity.ok(transactions);

        } catch (Exception e) {
            logger.warn("Unable to retrieve transactions");
            return ResponseEntity.internalServerError().body(new MessageResponse("Errore del server, riprova."));
        }
    }

    @PostMapping("/transfer")
    public ResponseEntity<?> transfer(@RequestHeader("Authorization") String token, @RequestBody TransferRequest request) throws IOException {

        logger.info(">>> Request new transfer from account " + request.accountNumberFrom() + " to " + request.accountNumberTo());

        TransferMessage message = new TransferMessage(request.accountNumberFrom(), request.accountNumberTo(), request.amount(), request.cause(), jwtUtils.getAccountOwnerIdFromJwtToken(token));

        BackTransferMessage response = sender.sendTransferRequest(message);

        if (response != null) {
            switch ((response).status()) {
                case 1:
                    TransactionDto transaction = new TransactionDto(request.type(), request.accountNumberFrom(), request.accountNumberTo(),
                            jwtUtils.getAccountOwnerIdFromJwtToken(token), response.accountOwnerIdTo(), Instant.now(), request.cause(), Math.abs(request.amount()));
                    transactionService.save(transaction);
                    logger.info("New transfer from account " + request.accountNumberFrom() + " to " + request.accountNumberTo() + " correctly saved");
                    return ResponseEntity.ok(transaction);
                case 2:
                    logger.warn("Unable to create new transfer: invalid amount");
                    return ResponseEntity.badRequest().body(new MessageResponse("Quantità non valida."));
                case 3:
                    logger.warn("Unable to create new transfer: invalid account numbers");
                    return ResponseEntity.badRequest().body(new MessageResponse("Numero/i di carta non valido/i."));
                case 4:
                    logger.warn("Unable to create new transfer: inactive account(s)");
                    return ResponseEntity.badRequest().body(new MessageResponse("Conto/i non attivi."));
                default:
                    logger.warn("Unable to create new transfer: server error");
                    return ResponseEntity.internalServerError().body(new MessageResponse("Errore del server, riprova."));
            }
        } else {
            logger.warn("Unable to create new transfer: error sending message");
            return ResponseEntity.internalServerError().body(new MessageResponse("Errore del server, riprova."));
        }
    }

    @PostMapping
    public ResponseEntity<?> transaction(@RequestHeader("Authorization") String token, @RequestBody TransactionRequest request) throws IOException {

        logger.info(">>> Request new transaction from account " + request.accountNumber());

        if ((request.type() == 2 && request.amount() <= 0) || (request.type() == 3 && request.amount() >= 0)) {
            return ResponseEntity.badRequest().body(new MessageResponse("Quantità non valida."));
        }

        int accountOwnerId = jwtUtils.getAccountOwnerIdFromJwtToken(token);

        TransactionMessage message = new TransactionMessage(request.accountNumber(), request.amount(), accountOwnerId);

        Object response = sender.sendTransactionRequest(message);

        switch ((int) response) {
            case 1:
                TransactionDto transaction = new TransactionDto(request.type(), request.accountNumber(), null,
                        accountOwnerId, -1, Instant.now(), request.cause(), Math.abs(request.amount()));
                transactionService.save(transaction);
                logger.info("New transaction from account " + request.accountNumber() + " correctly saved");
                return ResponseEntity.ok(transaction);
            case 2:
                logger.warn("Unable to create new transaction: invalid amount");
                return ResponseEntity.badRequest().body(new MessageResponse("Quantità non valida."));
            case 3:
                logger.warn("Unable to create new transaction: invalid account number");
                return ResponseEntity.badRequest().body(new MessageResponse("Numero di conto non valido."));
            case 4:
                logger.warn("Unable to create new transaction: inactive account");
                return ResponseEntity.badRequest().body(new MessageResponse("Conto non attivo."));
            default:
                logger.warn("Unable to create new transaction: server error");
                return ResponseEntity.internalServerError().body(new MessageResponse("Errore del server, riprova."));
        }
    }
}
