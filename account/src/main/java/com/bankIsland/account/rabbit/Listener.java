package com.bankIsland.account.rabbit;

import com.bankIsland.account.dto.AccountDto;
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

            AccountDto newAccount = new AccountDto(accountNumber, creationAccountMessage.firstName(), creationAccountMessage.lastName(), 0, 1, creationAccountMessage.accountOwnerId());
            accountService.save(newAccount);
//            throw new RuntimeException();
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    @RabbitListener(queues = "transferQueue")
    public BackTransferMessage listenTransfer(TransferMessage message) {

        try {
            AccountDto accountFrom = accountService.findByAccountNumber(message.accountNumberFrom());

            if(message.accountOwnerId()==accountFrom.getAccountOwnerId()) {
                AccountDto accountTo = accountService.findByAccountNumber(message.accountNumberTo());

                if(accountFrom.getStatus()!=0 || accountTo.getStatus()!=0){
                    return new BackTransferMessage(4, -1);
                }

                double balance = accountFrom.getBalance();

                if ((message.amount() < balance) && (message.amount() > 0)) {

                    accountFrom.setBalance(balance - message.amount());
                    accountTo.setBalance(message.amount());

                    accountService.save(accountFrom);
                    accountService.save(accountTo);

                    return new BackTransferMessage(1, accountTo.getAccountOwnerId());
                } else {
                    return new BackTransferMessage(2, -1);
                }
            } else {
                return new BackTransferMessage(3, -1);
            }
        } catch (NoSuchElementException e) {
            return new BackTransferMessage(4, -1);
        } catch (Exception e) {
            return new BackTransferMessage(5, -1);
        }
    }

    @RabbitListener(queues = "transactionQueue")
    public int listenTransaction(TransactionMessage message) {

        try {
            AccountDto account = accountService.findByAccountNumber(message.accountNumber());

            if(message.accountOwnerId()==account.getAccountOwnerId()) {

                if(account.getStatus()!=0){
                    return 4;
                }

                double balance = account.getBalance();

                if ((message.amount()!=0 && balance+message.amount() >= 0)) {

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
            return 3;
        } catch (Exception e) {
            return 5;
        }
    }

}