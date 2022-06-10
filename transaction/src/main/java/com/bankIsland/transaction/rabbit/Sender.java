package com.bankIsland.transaction.rabbit;

import com.bankIsland.transaction.rabbit.message.BackTransferMessage;
import com.bankIsland.transaction.rabbit.message.TransactionMessage;
import com.bankIsland.transaction.rabbit.message.TransferMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;

@Service
public class Sender {

    private static final Logger logger = LoggerFactory.getLogger(Sender.class);

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public BackTransferMessage sendTransferRequest(TransferMessage message) throws JsonProcessingException {

        try {
            Object response = rabbitTemplate.convertSendAndReceive("transferQueue", message);
            logger.info(">>> Message sent on 'transferQueue' queue");
            return new BackTransferMessage((int) ((LinkedHashMap<?,?>) response).get("status"),(int) ((LinkedHashMap<?,?>) response).get("accountOwnerIdTo"));
        } catch (Exception e) {
            logger.error("Unable to send message on 'transferQueue' queue");
            return null;
        }

    }

    public int sendTransactionRequest(TransactionMessage message) throws JsonProcessingException {

        try {
            Object response = rabbitTemplate.convertSendAndReceive("transactionQueue", message);
            logger.info(">>> Message sent on 'transactionQueue' queue");
            return (int) response;
        } catch (Exception e) {
            logger.error("Unable to send message on 'transactionQueue' queue");
            return -1;
        }
    }
}
