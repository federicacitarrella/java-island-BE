package com.bankisland.transaction.controller;

import com.bankisland.transaction.controller.request.TransferRequest;
import com.bankisland.transaction.jwt.JwtUtils;
import com.bankisland.transaction.rabbit.Sender;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private Sender sender;

    @PostMapping
    public void createNewTransaction(@RequestHeader("Authorization") String token, @RequestBody TransferRequest request) throws IOException {

        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
//        int accountOwnerId = jwtUtils.getUserIdFromJwtToken(token);
        Object success = sender.sendBalanceRequest(request);

        if(success) {

        }

        //return ResponseEntity.badRequest().body(new MessageResponse("Error: Invalid request."));
    }
    // PROPERTIES ####################
}
