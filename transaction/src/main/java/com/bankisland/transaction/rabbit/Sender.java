package com.bankIsland.transaction.rabbit;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Sender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public int sendTransferRequest(TransferMessage message) throws JsonProcessingException {

        Object response = rabbitTemplate.convertSendAndReceive("transferQueue", message);

        return (int) response;
    }

    public int sendTransactionRequest(TransactionMessage message) throws JsonProcessingException {

        Object response = rabbitTemplate.convertSendAndReceive("transactionQueue", message);

        return (int) response;
    }
}
