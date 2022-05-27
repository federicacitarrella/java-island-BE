package com.bankisland.transaction.controller;

import com.bankisland.transaction.controller.request.TransactionRequest;
import com.bankisland.transaction.controller.response.MessageResponse;
import com.bankisland.transaction.jwt.JwtUtils;
import com.bankisland.transaction.rabbit.Sender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private Sender sender;

    @PostMapping
    public void createNewTransaction(@RequestHeader("Authorization") String token, @RequestBody TransactionRequest request) throws IOException {

        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
//        int accountOwnerId = jwtUtils.getUserIdFromJwtToken(token);
        Double balance = sender.sendBalanceRequest(request.getAccountNumberFrom());

        if(balance > request.getAmount() && request.getAmount() > 0){
            try{
                balance -= request.getAmount();
                System.out.println(balance);
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        //return ResponseEntity.badRequest().body(new MessageResponse("Error: Invalid request."));
    }
    // PROPERTIES ####################
}
