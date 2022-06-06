package com.bankIsland.transaction.rabbit;

import com.bankIsland.transaction.dto.TransactionDto;
import com.bankIsland.transaction.rabbit.message.ClosingAccountMessage;
import com.bankIsland.transaction.rabbit.message.OpeningTransactionMessage;
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

        logger.info(">>> Message received on queue 'closingAccountQueue'");

        try {
            TransactionDto transactionDto = transactionService.findByAccountNumberTo(message.accountNumber());

            transactionService.save(new TransactionDto(1, transactionDto.getAccountNumberTo(), transactionDto.getAccountNumberFrom(), transactionDto.getAccountOwnerIdFrom(), transactionDto.getAccountOwnerIdFrom(), Instant.now(), "Chiusura conto " + transactionDto.getAccountNumberTo(), transactionDto.getAmount()));
            logger.info("Closing transaction correctly created for account " + message.accountNumber());

            return transactionDto.getAccountNumberFrom();

        } catch (NoSuchElementException e) {
            logger.warn("Unable to close account: opening transaction not found");
            return null;
        } catch (Exception e) {
            logger.error("Unable to create closing transaction");
            return null;
        }
    }

    @RabbitListener(queues = "openingTransactionQueue")
    public Boolean listenOpeningTransaction(OpeningTransactionMessage message) {

        logger.info(">>> Message received on queue 'openingTransactionQueue'");

        try {
            transactionService.save(new TransactionDto(1, message.accountNumberFrom(), message.accountNumberTo(), message.accountOwnerId(), message.accountOwnerId(), Instant.now(), "Apertura conto " + message.accountNumberTo(), message.amount()));
            logger.info("Opening transaction correctly created for account " + message.accountNumberTo());
            return true;
        } catch (Exception e) {
            logger.error("Unable to create opening transaction");
            return false;
        }
    }
}