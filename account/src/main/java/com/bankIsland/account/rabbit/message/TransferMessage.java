package com.bankIsland.account.rabbit.message;

public record TransferMessage (String accountNumberFrom,
                              String accountNumberTo,
                              double amount,
                              String cause,
                              int accountOwnerId) {}