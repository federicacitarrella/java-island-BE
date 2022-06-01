package com.bankIsland.transaction.rabbit;

public record TransactionMessage (String accountNumber,
                                 double amount,
                                 int accountOwnerId) {}