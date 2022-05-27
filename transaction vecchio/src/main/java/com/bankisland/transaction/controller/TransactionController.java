package com.bankisland.transaction.controller;

import com.bankisland.transaction.controller.request.TransactionRequest;
import com.bankisland.transaction.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping
    public void createNewTransaction(@RequestHeader("Authorization") String token, @RequestBody TransactionRequest request) throws IOException {

        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        //int accountOwnerId = jwtUtils.getUserIdFromJwtToken(token);

        URL url = new URL("http://localhost:8765/api/accounts/balance/"+request.getAccountNumberFrom());
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("Authorization", token);
        connection.setRequestMethod("GET");

        int status = connection.getResponseCode();

        System.out.println(status);
        //return ResponseEntity.badRequest().body(new MessageResponse("Error: Invalid request."));
    }
    // PROPERTIES ####################
}
