package com.bankIsland.user.rabbit;

import com.bankIsland.user.rabbit.message.CreationAccountMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class Sender {

    private RabbitTemplate rabbitTemplate;

    private static final Logger logger = LoggerFactory.getLogger(Sender.class);

    public Sender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public boolean send(CreationAccountMessage message) {
        try {
            logger.info(">>> Sending message on 'accountCreationQueue' queue");
            return (boolean) rabbitTemplate.convertSendAndReceive("accountCreationQueue", message);
        } catch (Exception e) {
            logger.error("Unable to send account creation message on 'accountCreationQueue' queue");
            return false;
        }
    }
}
