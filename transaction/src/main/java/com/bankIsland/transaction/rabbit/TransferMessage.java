package com.bankIsland.transaction.rabbit;

public record TransferMessage (String accountNumberFrom,
                              String accountNumberTo,
                              double amount,
                              String cause,
                              int accountOwnerId) {}