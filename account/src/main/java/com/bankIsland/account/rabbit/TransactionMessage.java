package com.bankIsland.account.rabbit;

public record TransactionMessage (String accountNumber,
                                 double amount,
                                 int accountOwnerId) {}