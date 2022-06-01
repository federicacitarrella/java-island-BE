package com.bankIsland.transaction.rabbit;

public record OpeningTransactionMessage (int accountOwnerId,
                                        String accountNumberFrom,
                                        String accountNumberTo,
                                        double amount) {}