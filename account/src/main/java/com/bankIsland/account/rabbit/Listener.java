package com.bankIsland.account.rabbit;

import com.bankIsland.account.dto.AccountDto;
import com.bankIsland.account.rabbit.message.BackTransferMessage;
import com.bankIsland.account.rabbit.message.CreationAccountMessage;
import com.bankIsland.account.rabbit.message.TransactionMessage;
import com.bankIsland.account.rabbit.message.TransferMessage;
import com.bankIsland.account.service.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Random;

@Service
public class Listener {

    @Autowired
    private AccountService accountService;

    private static final Logger logger = LoggerFactory.getLogger(Listener.class);


    @RabbitListener(queues = "accountCreationQueue")
    public Boolean listen(CreationAccountMessage creationAccountMessage) {

        logger.info(">>> Message received on queue 'accountCreationQueue'");

        try {
            Random rand = new Random();
            String accountNumber;
            do {
                accountNumber = "IT" + rand.nextInt(1000000000);
            } while (accountService.existsByAccountNumber(accountNumber));

            AccountDto newAccount = new AccountDto(accountNumber, creationAccountMessage.firstName(), creationAccountMessage.lastName(), 0, 1, creationAccountMessage.accountOwnerId());
            accountService.save(newAccount);
//            throw new RuntimeException();
            return true;
        } catch (Exception e) {
            logger.error("Unable to perform account creation");
            return false;
        }
    }

    @RabbitListener(queues = "transferQueue")
    public Object listenTransfer(TransferMessage message) {

        logger.info(">>> Message received on queue 'transferQueue'");

        try {
            AccountDto accountFrom = accountService.findByAccountNumber(message.accountNumberFrom());

            if(message.accountOwnerId()==accountFrom.getAccountOwnerId()) {
                AccountDto accountTo = accountService.findByAccountNumber(message.accountNumberTo());

                if(accountFrom.getStatus()!=0 || accountTo.getStatus()!=0){
                    logger.warn("Unable to perform transfer: inactive account(s)");
                    return new BackTransferMessage(4, -1);
                }

                double balance = accountFrom.getBalance();

                if ((message.amount() <= balance) && (message.amount() > 0) && ((accountTo.getBalance() + message.amount())<=99999999.99)) {

                    accountFrom.setBalance(balance - message.amount());
                    accountTo.setBalance(accountTo.getBalance() + message.amount());

                    accountService.save(accountFrom);
                    accountService.save(accountTo);

                    logger.info("Transfer correctly performed");
                    return new BackTransferMessage(1, accountTo.getAccountOwnerId());
                } else {
                    logger.warn("Unable to perform transfer: invalid amount");
                    return new BackTransferMessage(2, -1);
                }
            } else {
                return new BackTransferMessage(3, -1);
            }
        } catch (NoSuchElementException e) {
            logger.warn("Unable to find the account(s)");
            return new BackTransferMessage(4, -1);
        } catch (Exception e) {
            logger.error("Unable to perform the transfer");
            return new BackTransferMessage(5, -1);
        }
    }

    @RabbitListener(queues = "transactionQueue")
    public int listenTransaction(TransactionMessage message) {

        logger.info(">>> Message received on queue 'transactionQueue'");

        try {
            AccountDto account = accountService.findByAccountNumber(message.accountNumber());

            if(message.accountOwnerId()==account.getAccountOwnerId()) {

                if(account.getStatus()!=0){
                    return 4;
                }

                double balance = account.getBalance();

                if ((message.amount()!=0) && (balance+message.amount() >= 0) && (balance + message.amount()<99999999.99)) {

                    account.setBalance(balance + message.amount());
                    accountService.save(account);

                    return 1;
                } else {
                    return 2;
                }
            } else {
                return 3;
            }
        } catch (NoSuchElementException e) {
            logger.warn("Unable to find the account");
            return 3;
        } catch (Exception e) {
            logger.error("Unable to perform the transaction");
            return 5;
        }
    }

}