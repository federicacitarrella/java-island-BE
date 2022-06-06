package com.bankIsland.account.rabbit;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Sender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public String closingAccountRequest(ClosingAccountMessage message) {

        Object response = rabbitTemplate.convertSendAndReceive("closingAccountQueue", message);

        return (String) response;
    }

    public Boolean openingTransactionRequest(OpeningTransactionMessage message) {

        Object response = rabbitTemplate.convertSendAndReceive("openingTransactionQueue", message);

        return (Boolean) response;
    }

}