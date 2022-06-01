package com.bankIsland.user.controller.payload.response;

import com.bankIsland.user.dto.AccountOwnerDto;

public record UserLoginResponse (String token,
        AccountOwnerDto accountOwnerDto,
        String role) {}