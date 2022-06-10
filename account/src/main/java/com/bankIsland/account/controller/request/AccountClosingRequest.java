package com.bankIsland.account.controller.request;

public record AccountClosingRequest (String accountNumberClosing,
        String accountNumberTo) {}
