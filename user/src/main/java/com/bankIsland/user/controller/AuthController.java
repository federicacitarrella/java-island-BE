package com.bankIsland.user.controller;

import com.bankIsland.user.controller.payload.request.LoginRequest;
import com.bankIsland.user.controller.payload.request.SignupRequest;
import com.bankIsland.user.controller.payload.response.EmployeeLoginResponse;
import com.bankIsland.user.controller.payload.response.MessageResponse;
import com.bankIsland.user.controller.payload.response.UserLoginResponse;
import com.bankIsland.user.dto.AccountOwnerDto;
import com.bankIsland.user.exception.ApiBankException;
import com.bankIsland.user.rabbit.Sender;
import com.bankIsland.user.rabbit.message.CreationAccountMessage;
import com.bankIsland.user.security.jwt.JwtUtils;
import com.bankIsland.user.security.service.UserDetailsImpl;
import com.bankIsland.user.service.AccountOwnerService;
import com.bankIsland.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
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
                return ResponseEntity.badRequest().body(new MessageResponse("Username e/o password errate."));
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
            return ResponseEntity.internalServerError().body(new MessageResponse("Errore del server, riprova."));
        }
    }

    @PostMapping("/signup")
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signupRequest) throws ApiBankException {

        logger.info(">>> Request signup");

        try {

            if (userService.existsByUsername(signupRequest.email()) || accountOwnerService.existsByEmail(signupRequest.email())) {
                logger.warn("Email " + signupRequest.email() + " not available");
                return ResponseEntity.badRequest().body(new MessageResponse("Email già registrata nel sistema."));
            }

            AccountOwnerDto accountOwnerDto = accountOwnerService.registration(signupRequest);
            userService.registration(signupRequest, accountOwnerDto.getId());

            if(sender.send(new CreationAccountMessage(accountOwnerDto.getId(), accountOwnerDto.getFirstName(), accountOwnerDto.getLastName()))){
                logger.info("New user correctly saved");
                return ResponseEntity.ok(new MessageResponse("Registrazione avvenuta con successo."));
            } else {
                logger.error("Unable to create new account");
            }

        } catch (Exception e) {
            logger.error("Unable to create new user");
        }

        throw new ApiBankException("Errore: impossibile creare un nuovo account.");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationExceptions(MethodArgumentNotValidException ex) {

        List errors = ex.getBindingResult().getAllErrors();
        List<String> fields = new ArrayList<>();

        for (Object error : errors) {
            fields.add(((FieldError) error).getField());
        }

        String message = String.join(", ", fields);
        return ResponseEntity.badRequest().body(new MessageResponse("Dati inseriti non validi (" + message + ")"));
    }

}
