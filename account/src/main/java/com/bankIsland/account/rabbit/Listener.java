package com.bankIsland.account.rabbit;

import com.bankIsland.account.entity.Account;
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

        logger.info(">>>>>> creating Account");

        try {
            Random rand = new Random();
            String accountNumber;
            do {
                accountNumber = "IT" + rand.nextInt(1000000000);
            } while (accountService.existsByAccountNumber(accountNumber));

            Account newAccount = new Account(accountNumber, creationAccountMessage.getFirstName(), creationAccountMessage.getLastName(), 0, 1, creationAccountMessage.getAccountOwnerId());
            accountService.save(newAccount);
//            throw new RuntimeException();
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    @RabbitListener(queues = "transferQueue")
    public int listenTransfer(TransferMessage message) {

        try {
            Account accountFrom = accountService.findByAccountNumber(message.getAccountNumberFrom());

            if(message.getAccountOwnerId()==accountFrom.getAccountOwnerId()) {
                Account accountTo = accountService.findByAccountNumber(message.getAccountNumberTo());

                if(accountFrom.getStatus()!=0 || accountTo.getStatus()!=0){
                    return 4;
                }

                double balance = accountFrom.getBalance();

                if ((message.getAmount() < balance) && (message.getAmount() > 0)) {

                    accountFrom.setBalance(balance - message.getAmount());
                    accountTo.setBalance(message.getAmount());

                    accountService.save(accountFrom);
                    accountService.save(accountTo);

                    return 1;
                } else {
                    return 2;
                }
            } else {
                return 3;
            }
        } catch (NoSuchElementException e) {
            return 3;
        } catch (Exception e) {
            return 5;
        }
    }

    @RabbitListener(queues = "transactionQueue")
    public int listenTransaction(TransactionMessage message) {

        try {
            Account account = accountService.findByAccountNumber(message.getAccountNumber());

            if(message.getAccountOwnerId()==account.getAccountOwnerId()) {

                if(account.getStatus()!=0){
                    return 4;
                }

                double balance = account.getBalance();

                if ((balance+message.getAmount() > 0)) {

                    account.setBalance(balance + message.getAmount());
                    accountService.save(account);

                    return 1;
                } else {
                    return 2;
                }
            } else {
                return 3;
            }
        } catch (NoSuchElementException e) {
            return 3;
        } catch (Exception e) {
            return 5;
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
}