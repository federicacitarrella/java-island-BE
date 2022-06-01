package com.bankIsland.account.rabbit;

public record TransferMessage (String accountNumberFrom,
                              String accountNumberTo,
                              double amount,
                              String cause,
                              int accountOwnerId) {}