package com.bankIsland.account.rabbit;

import com.bankIsland.account.rabbit.message.ClosingAccountMessage;
import com.bankIsland.account.rabbit.message.OpeningTransactionMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Sender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private static final Logger logger = LoggerFactory.getLogger(Sender.class);

    public String closingAccountRequest(ClosingAccountMessage message) {

        try {
            Object response = rabbitTemplate.convertSendAndReceive("closingAccountQueue", message);
            logger.info(">>> Message sent on queue 'closingAccountQueue'");
            return (String) response;
        } catch (Exception e) {
            logger.error("Unable to send message on 'closingAccountQueue' queue");
            return null;
        }
    }

    public Boolean openingTransactionRequest(OpeningTransactionMessage message) {

        try {
            Object response = rabbitTemplate.convertSendAndReceive("openingTransactionQueue", message);
            logger.info(">>> Message sent on 'openingTransactionQueue' queue");
            return (Boolean) response;
        } catch (Exception e) {
            logger.error("Unable to send message on 'openingTransactionQueue' queue");
            return false;
        }
    }
}
