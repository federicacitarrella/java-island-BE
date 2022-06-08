package com.bankIsland.account.rabbit.message;

public record BackTransferMessage(int status,
                                  int accountOwnerIdTo) {}