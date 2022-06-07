package com.bankIsland.user.controller.payload.request;

import java.time.LocalDate;

public record SignupRequest (String firstName,
                             String lastName,
                             String email,
                             LocalDate birthDate,
                             String password) {}