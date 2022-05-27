package com.bankisland.transaction.rabbit;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Sender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public Double sendBalanceRequest(String message) throws JsonProcessingException {

        Object balance = rabbitTemplate.convertSendAndReceive("balanceQueue", message);
        return (Double) balance;
    }
}
