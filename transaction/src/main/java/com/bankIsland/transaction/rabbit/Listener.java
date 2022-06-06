package com.bankIsland.transaction.rabbit;

import com.bankIsland.transaction.dto.TransactionDto;
import com.bankIsland.transaction.service.TransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.NoSuchElementException;

@Service
public class Listener {

    @Autowired
    private TransactionService transactionService;

    private static final Logger logger = LoggerFactory.getLogger(Listener.class);

    @RabbitListener(queues = "closingAccountQueue")
    public String listenClosingAccount(ClosingAccountMessage message) {

        try {
            TransactionDto transactionDto = transactionService.findByAccountNumberTo(message.accountNumber());

            transactionService.save(new TransactionDto(1, transactionDto.getAccountNumberTo(), transactionDto.getAccountNumberFrom(), transactionDto.getAccountOwnerIdFrom(), transactionDto.getAccountOwnerIdFrom(), Instant.now(), "Chiusura conto " + transactionDto.getAccountNumberTo(), transactionDto.getAmount()));

            return transactionDto.getAccountNumberFrom();

        } catch (NoSuchElementException e) {
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    @RabbitListener(queues = "openingTransactionQueue")
    public Boolean listenOpeningTransaction(OpeningTransactionMessage message) {

        try {
            transactionService.save(new TransactionDto(1, message.accountNumberFrom(), message.accountNumberTo(), message.accountOwnerId(), message.accountOwnerId(), Instant.now(), "Apertura conto " + message.accountNumberTo(), message.amount()));
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}

//    @RabbitListener(queues = "accountCreationQueue")
//    public void listen(CreationAccountMessage message) {
//
//        logger.info(">>>>>> creating Account");
//
//        int accountOwnerId = message.getAccountOwnerId();
//
//        Random rand = new Random();
//        String accountNumber;
//        do {
//            accountNumber = "IT" + rand.nextInt(1000000000);
//        } while (accountRepository.existsByAccountNumber(accountNumber));
//
//        Account newAccount = new Account(accountNumber, 0, 1, accountOwnerId);
//        accountRepository.save(newAccount);
//    }