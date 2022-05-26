package com.bankIsland.account.rabbit;

import com.bankIsland.account.dao.AccountRepository;
import com.bankIsland.account.entity.Account;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class Listener {

    @Autowired
    private AccountRepository accountRepository;

    private static final Logger logger = LoggerFactory.getLogger(Listener.class);


    @RabbitListener(queues = "accountCreationQueue")
    public void listen(String accountOwnerId) {

        logger.info(">>>>>> creating Account");

        Random rand = new Random();
        String accountNumber;
        do {
            accountNumber = "IT" + rand.nextInt(1000000000);
        } while (accountRepository.existsByAccountNumber(accountNumber));

        Account newAccount = new Account(accountNumber, 0, 1, Integer.parseInt(accountOwnerId));
        accountRepository.save(newAccount);
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