package com.bankIsland.transaction.rabbit.message;

public record TransactionMessage (String accountNumber,
                                 double amount,
                                 int accountOwnerId) {}