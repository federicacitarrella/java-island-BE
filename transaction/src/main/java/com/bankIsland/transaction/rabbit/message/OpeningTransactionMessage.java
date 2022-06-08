package com.bankIsland.transaction.rabbit.message;

public record OpeningTransactionMessage (int accountOwnerId,
                                        String accountNumberFrom,
                                        String accountNumberTo,
                                        double amount) {}