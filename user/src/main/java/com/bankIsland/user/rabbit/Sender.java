package com.bankIsland.user.rabbit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Sender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private ObjectMapper objectMapper;

    public void send(CreationAccountMessage message) throws JsonProcessingException {
//        String messageJson = objectMapper.writeValueAsString(message);
//        Message messageToSend = MessageBuilder
//                .withBody(messageJson.getBytes())
//                .setContentType(MessageProperties.CONTENT_TYPE_JSON)
//                .build();
        rabbitTemplate.convertAndSend("accountCreationQueue", message);
    }
}
