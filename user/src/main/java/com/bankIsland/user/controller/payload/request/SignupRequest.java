package com.bankIsland.user.controller.payload.request;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

public record SignupRequest (@NotBlank(message = "Firstname is mandatory") String firstName,
                             @NotBlank(message = "Lastname is mandatory") String lastName,
                             @NotBlank(message = "Email is mandatory") String email,
                             @NotBlank(message = "Birth date is mandatory") LocalDate birthDate,
                             @NotBlank(message = "Password is mandatory") String password) {}