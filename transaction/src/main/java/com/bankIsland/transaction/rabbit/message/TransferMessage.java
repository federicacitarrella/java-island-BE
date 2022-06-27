package com.bankIsland.transaction.rabbit.message;

public record TransferMessage(String accountNumberFrom,
                              String accountNumberTo,
                              double amount,
                              String cause,
                              int accountOwnerId) {
}