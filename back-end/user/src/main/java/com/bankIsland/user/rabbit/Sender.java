package com.bankIsland.user.rabbit;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Sender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void send(String accountOwnerId) {
        rabbitTemplate.convertAndSend("accountCreationQueue", accountOwnerId);
    }
}
