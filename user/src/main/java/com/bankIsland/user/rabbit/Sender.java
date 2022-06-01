package com.bankIsland.user.rabbit;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class Sender {

    private RabbitTemplate rabbitTemplate;

    public Sender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public boolean send(CreationAccountMessage message, int userId) {
        try {
            return (boolean) rabbitTemplate.convertSendAndReceive("accountCreationQueue", message);
        } catch (Exception e) {
            return false;
        }
    }
}
