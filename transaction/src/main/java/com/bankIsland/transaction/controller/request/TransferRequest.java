package com.bankIsland.transaction.controller.request;

public record TransferRequest (String accountNumberFrom,
                               String accountNumberTo,
                               int type,
                               double amount,
                               String cause) {}