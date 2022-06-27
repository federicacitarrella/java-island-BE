package com.bankIsland.account.rabbit.message;

public record TransactionMessage (String accountNumber,
                                 double amount,
                                 int accountOwnerId) {}