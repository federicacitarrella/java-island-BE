package com.bankIsland.account.rabbit;

public record OpeningTransactionMessage (int accountOwnerId,
                                        String accountNumberFrom,
                                        String accountNumberTo,
                                        double amount) {}