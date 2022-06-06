package com.bankIsland.user.controller;

import com.bankIsland.user.controller.payload.response.MessageResponse;
import com.bankIsland.user.exception.ApiBankException;
import com.bankIsland.user.security.jwt.JwtUtils;
import com.bankIsland.user.service.AccountOwnerService;
import com.bankIsland.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/account_owners")
public class AccountOwnerController {

    private final AccountOwnerService accountOwnerService;
    private final UserService userService;
    private JwtUtils jwtUtils;

    private static final Logger logger = LoggerFactory.getLogger(AccountOwnerController.class);

    public AccountOwnerController(AccountOwnerService accountOwnerService, UserService userService, JwtUtils jwtUtils) {
        this.accountOwnerService = accountOwnerService;
        this.userService = userService;
        this.jwtUtils = jwtUtils;
    }

    @DeleteMapping
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<?> deleteUser(@RequestHeader("Authorization") String token) throws ApiBankException{
        try {
            String accountOwnerEmail = jwtUtils.getUserNameFromJwtToken(token);
            accountOwnerService.delete(accountOwnerService.findByEmail(accountOwnerEmail));
            userService.delete(userService.findByUsername(accountOwnerEmail));
            return ResponseEntity.ok(new MessageResponse("User correctly deleted."));
        } catch (Exception e) {
            throw new ApiBankException("Error: enable to delete the user.");
        }
    }

    @GetMapping("/intern")
    public ResponseEntity<?> registrationList(){
        logger.info(">>> Request users list");
        try {
            return ResponseEntity.ok(accountOwnerService.findAll());
        } catch (Exception e) {
            logger.error("Unable to return users list");
            return ResponseEntity.internalServerError().body(new MessageResponse("Server error, try later."));
        }
    }
}
