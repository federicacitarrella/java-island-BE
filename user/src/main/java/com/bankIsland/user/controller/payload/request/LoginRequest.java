package com.bankIsland.user.controller.payload.request;

public record LoginRequest (String username,
                           String password) {}