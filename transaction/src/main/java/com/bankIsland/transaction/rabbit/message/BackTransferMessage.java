package com.bankIsland.transaction.rabbit.message;

public record BackTransferMessage(int status,
                                  int accountOwnerIdTo) {
}