package com.bankIsland.transaction.rabbit;

public record BackTransferMessage(int status,
                                  int accountOwnerIdTo) {}