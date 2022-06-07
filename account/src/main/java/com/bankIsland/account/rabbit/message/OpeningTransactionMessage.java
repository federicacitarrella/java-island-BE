package com.bankIsland.account.rabbit.message;

public record OpeningTransactionMessage (int accountOwnerId,
                                        String accountNumberFrom,
                                        String accountNumberTo,
                                        double amount) {}