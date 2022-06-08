package com.bankIsland.account.controller;

import com.bankIsland.account.controller.request.AccountCreationRequest;
import com.bankIsland.account.controller.response.MessageResponse;
import com.bankIsland.account.dto.AccountDto;
import com.bankIsland.account.exception.ApiBankException;
import com.bankIsland.account.rabbit.message.ClosingAccountMessage;
import com.bankIsland.account.rabbit.message.OpeningTransactionMessage;
import com.bankIsland.account.rabbit.Sender;
import com.bankIsland.account.security.jwt.JwtUtils;
import com.bankIsland.account.service.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final JwtUtils jwtUtils;
    private final AccountService accountService;
    private final Sender sender;

    public AccountController(JwtUtils jwtUtils, AccountService accountService, Sender sender) {
        this.jwtUtils = jwtUtils;
        this.accountService = accountService;
        this.sender = sender;
    }

    private static final Logger logger = LoggerFactory.getLogger(AccountController.class);

    @PostMapping
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<?> createAccount(@RequestHeader("Authorization") String token, @RequestBody AccountCreationRequest request) throws ApiBankException {
        logger.info(">>> Request creation new account");

        try {

            int accountOwnerId = jwtUtils.getAccountOwnerIdFromJwtToken(token);

            if (accountService.isFirstAccount(accountOwnerId)) {
                logger.info("New account creation");
                return ResponseEntity.ok(accountService.createFirstAccount(accountOwnerId, request));
            }

            AccountDto account = accountService.findByAccountNumber(request.accountNumber());

            if (accountOwnerId == account.getAccountOwnerId()) {
                if (request.amount() <= account.getBalance() && request.amount() > 0) {

                    AccountDto newAccount = accountService.createAccount(account, request);
                    logger.info("Account " + newAccount.getAccountNumber() + " creation");

                    OpeningTransactionMessage message = new OpeningTransactionMessage(accountOwnerId, account.getAccountNumber(), newAccount.getAccountNumber(), request.amount());
                    if (sender.openingTransactionRequest(message)) {
                        logger.info("Account creation transaction done");
                        return ResponseEntity.ok(newAccount);
                    } else {
                        logger.error("Unable to perform the creation transaction");
                        throw new ApiBankException("Errore: impossibile creare la nuova transazione.");
                    }

                } else {
                    logger.warn("Unable to create new account: invalid amount");
                    return ResponseEntity.badRequest().body(new MessageResponse("Errore: quantità non valida."));
                }
            } else {
                logger.warn("Unable to create new account: invalid account number");
                return ResponseEntity.badRequest().body(new MessageResponse("Errore: numero di conto non valido."));
            }
        } catch (NoSuchElementException e) {
            logger.warn("Unable to create new account: invalid account number");
            return ResponseEntity.badRequest().body(new MessageResponse("Errore: numero di conto non valido."));
        }
    }

    @PutMapping("/{account_id}")
    public ResponseEntity<?> closureAccount(@RequestHeader("Authorization") String token, @PathVariable int account_id) {
        logger.info(">>> Request account closure");

        int accountOwnerId = jwtUtils.getAccountOwnerIdFromJwtToken(token);

        try {
            AccountDto account = accountService.findById(account_id);

            if (accountOwnerId == account.getAccountOwnerId()) {
                if (accountService.closeAccount(account)) {
                    logger.info("Account " + account_id + " closure request correctly send");
                    return ResponseEntity.ok(new MessageResponse("Richiesta chiusura conto inviata con successo."));
                } else {
                    logger.error("Unable to request account " + account_id + " closure");
                    return ResponseEntity.badRequest().body(new MessageResponse("Errore: verificare che il bilancio della carta sia 0."));
                }
            }

        } catch (NoSuchElementException e) {
            logger.warn("Unable to request account " + account_id + " closure: invalid account number");
            return ResponseEntity.badRequest().body(new MessageResponse("Errore: numero di conto non valido."));
        }
        logger.error("Unable to request account " + account_id + " closure");
        return ResponseEntity.internalServerError().body(new MessageResponse("Errore del server, riprova."));
    }

    @GetMapping
    public ResponseEntity<?> getAccounts(@RequestHeader("Authorization") String token) {
        logger.info(">>> Request list accounts");
        int accountOwnerId = -1;
        try {
             accountOwnerId = jwtUtils.getAccountOwnerIdFromJwtToken(token);
            return ResponseEntity.ok(accountService.findAllByAccountOwnerId(accountOwnerId));
        } catch (NoSuchElementException e) {
            logger.warn("Unable to find accounts for "+ accountOwnerId);
            return ResponseEntity.badRequest().body(new MessageResponse("Nessun account disponibile."));
        } catch (Exception e) {
            logger.error("Unable to find accounts for " + accountOwnerId);
            return ResponseEntity.internalServerError().body(new MessageResponse("Errore del server, riprova."));
        }
    }

    @GetMapping("/intern/registrations")
    public ResponseEntity<?> registrationList() {
        logger.info(">>> Request list registration accounts");
        try {
            return ResponseEntity.ok(accountService.findAllByStatus(1));
        } catch (Exception e) {
            logger.error("Unable to find registration requests");
            return ResponseEntity.internalServerError().body(new MessageResponse("Errore del server, riprova."));
        }
    }

    @GetMapping("/intern/opening_accounts")
    public ResponseEntity<?> openingList() {
        logger.info(">>> Request list multiple accounts");
        try {
            return ResponseEntity.ok(accountService.findAllByStatus(2));
        } catch (NoSuchElementException e) {
            logger.warn("Unable to find accounts");
            return ResponseEntity.badRequest().body(new MessageResponse("Nessun account disponibile."));
        } catch (Exception e) {
            logger.error("Unable to find accounts");
            return ResponseEntity.internalServerError().body(new MessageResponse("Errore del server, riprova."));
        }    }

    @GetMapping("/intern/closing_accounts")
    public ResponseEntity<?> closingList() {
        logger.info(">>> Request list closing accounts");
        try {
            return ResponseEntity.ok(accountService.findAllByStatus(3));
        } catch (NoSuchElementException e) {
            logger.warn("Unable to find accounts");
            return ResponseEntity.badRequest().body(new MessageResponse("Nessun account disponibile."));
        } catch (Exception e) {
            logger.error("Unable to find accounts");
            return ResponseEntity.internalServerError().body(new MessageResponse("Errore del server, riprova."));
        }
    }

    @PutMapping("/intern/validation/{account_id}")
    public ResponseEntity<?> validateAccount(@PathVariable int account_id) {
        try {
            logger.info(">>> Request validation account");
            if (accountService.validateAccount(account_id)){
                logger.info("Account " + account_id + " correctly validate");
                return ResponseEntity.ok(new MessageResponse("Successo."));
            } else {
                logger.error("Unable to validate account " + account_id);
            }
        } catch (Exception e) {
            logger.error("Unable to validate account " + account_id);
        }
        return ResponseEntity.internalServerError().body(new MessageResponse("Errore del server, riprova."));
    }

    @PutMapping("/intern/rejection/{account_id}")
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<?> rejectAccount(@PathVariable int account_id) {
        logger.info(">>> Request rejection account");
        try {
            AccountDto account = accountService.findById(account_id);

            if (account.getStatus() == 3) {
                logger.info(">>> Request validation account closure");
                if(accountService.rejectAccount(account)) {
                    logger.info("Account " + account_id + " correctly closed");
                    return ResponseEntity.ok(new MessageResponse("Successo."));
                } else {
                    logger.error("Unable to close account " + account_id);
                    return ResponseEntity.internalServerError().body(new MessageResponse("Errore del server, riprova."));
                }

                //ATT ROLLBACK
            } else if (account.getStatus() == 2) {
                logger.info(">>> Request rejection new account");
                String accountNumberPrev = sender.closingAccountRequest(new ClosingAccountMessage(account.getAccountNumber()));
                if (accountNumberPrev != null) {
                    AccountDto accountPrev = accountService.findByAccountNumber(accountNumberPrev);
                    if (accountService.rejectNewAccount(account, accountPrev)) {
                        logger.info("Account " + account_id + " correctly rejected");
                        return ResponseEntity.ok(new MessageResponse("Successo"));
                    } else {
                        logger.error("Unable to reject account " + account_id);
                        return ResponseEntity.internalServerError().body(new MessageResponse("Errore del server, riprova."));
                    }
                } else {
                    logger.error("Unable to perform the transaction");
                    return ResponseEntity.internalServerError().body(new MessageResponse("Errore del server, riprova."));
                }
            }
            logger.warn("Unable to reject account " + account_id + ": action not available");
            return ResponseEntity.badRequest().body(new MessageResponse("Azione non disponibile per questo conto."));

        } catch (Exception e) {
            logger.error("Unable to reject account " + account_id);
            return ResponseEntity.internalServerError().body(new MessageResponse("Errore del server, riprova."));
        }
    }

    @DeleteMapping("/{account_id}")
    public ResponseEntity<?> deleteAccount(@PathVariable int account_id) {
        logger.info(">>> Request deletion account");
        try {
            if (accountService.delete(accountService.findById(account_id))) {
                logger.info("Account " + account_id + " correctly deleted");
                return ResponseEntity.ok(new MessageResponse("Success"));
            } else {
                logger.error("Unable to delete account " + account_id);
                return ResponseEntity.badRequest().body(new MessageResponse("Azione non disponibile per questo conto."));
            }
        } catch (NoSuchElementException e) {
            logger.warn("Unable to find account " + account_id);
            return ResponseEntity.badRequest().body(new MessageResponse("Conto non trovato."));
        } catch (Exception e) {
            logger.error("Unable to delete account " + account_id);
            return ResponseEntity.internalServerError().body(new MessageResponse("Errore del server, riprova."));
        }
    }
}
