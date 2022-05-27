package com.bankIsland.user.rabbit;

import com.bankIsland.user.service.AccountOwnerService;
import com.bankIsland.user.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Sender {

    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private AccountOwnerService accountOwnerService;
    @Autowired
    private UserService userService;

    public boolean send(CreationAccountMessage message, int userId) throws JsonProcessingException {

        Object success = rabbitTemplate.convertSendAndReceive("accountCreationQueue", message);

        if(!(Boolean) success) {
            accountOwnerService.deleteById(message.getAccountOwnerId());
            userService.deleteById(userId);
            return false;
        }
        return true;
    }
}
