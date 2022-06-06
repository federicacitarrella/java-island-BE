package com.bankIsland.user.controller;

import com.bankIsland.user.controller.payload.request.LoginRequest;
import com.bankIsland.user.controller.payload.request.SignupRequest;
import com.bankIsland.user.controller.payload.response.EmployeeLoginResponse;
import com.bankIsland.user.controller.payload.response.MessageResponse;
import com.bankIsland.user.controller.payload.response.UserLoginResponse;
import com.bankIsland.user.dto.AccountOwnerDto;
import com.bankIsland.user.dto.UserDto;
import com.bankIsland.user.exception.ApiBankException;
import com.bankIsland.user.rabbit.message.CreationAccountMessage;
import com.bankIsland.user.rabbit.Sender;
import com.bankIsland.user.security.jwt.JwtUtils;
import com.bankIsland.user.security.service.UserDetailsImpl;
import com.bankIsland.user.service.AccountOwnerService;
import com.bankIsland.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    AuthenticationManager authenticationManager;
    UserService userService;
    AccountOwnerService accountOwnerService;
    JwtUtils jwtUtils;
    private final Sender sender;

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    public AuthController(AuthenticationManager authenticationManager, UserService userService, AccountOwnerService accountOwnerService, JwtUtils jwtUtils, Sender sender) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.accountOwnerService = accountOwnerService;
        this.jwtUtils = jwtUtils;
        this.sender = sender;
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        logger.info(">>> Request signin");

        try {
            Authentication authentication;

            try {
                authentication = authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(loginRequest.username(), loginRequest.password()));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (BadCredentialsException e) {
                logger.error("Invalid credentials");
                return ResponseEntity.badRequest().body(new MessageResponse("Invalid credentials."));
            }

            String token = jwtUtils.generateJwtToken(authentication);
            String username = jwtUtils.getUserNameFromJwtToken(token);

            List<String> roles = ((UserDetailsImpl) authentication.getPrincipal()).getAuthorities()
                    .stream()
                    .map(GrantedAuthority::getAuthority)
                    .toList();

            if (roles.get(0).equals("D")) {
                logger.info("Employee " + username + " correctly loaded");
                return ResponseEntity.ok(new EmployeeLoginResponse(token,
                        userService.findByUsername(loginRequest.username()),
                        roles.get(0)));
            } else {
                logger.info("User " + username + " correctly loaded");
                return ResponseEntity.ok(new UserLoginResponse(token,
                        accountOwnerService.findByEmail(loginRequest.username()),
                        roles.get(0)));
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new MessageResponse("Server error, try later."));
        }
    }

    @PostMapping("/signup")
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signupRequest) throws ApiBankException {
        logger.info(">>> Request signup");

        try {
            if (userService.existsByUsername(signupRequest.email()) || accountOwnerService.existsByEmail(signupRequest.email())) {
                logger.warn("Email " + signupRequest.email() + " not available");
                return ResponseEntity.badRequest().body(new MessageResponse("Email already used!"));
            }

            AccountOwnerDto accountOwnerDto = accountOwnerService.registration(signupRequest);
            UserDto userDto = userService.registration(signupRequest, accountOwnerDto.getId());

            if(sender.send(new CreationAccountMessage(accountOwnerDto.getId(), accountOwnerDto.getFirstName(), accountOwnerDto.getLastName()), userDto.getId())) {
                logger.info("New user correctly saved");
                return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
            } else {
                logger.error("Unable to create new account");
                throw new ApiBankException("Unable to create new account.");
            }
        } catch (Exception e) {
            logger.warn("Exception handling");
            return ResponseEntity.internalServerError().body(new MessageResponse("Server error, try later."));
        }

    }

}
