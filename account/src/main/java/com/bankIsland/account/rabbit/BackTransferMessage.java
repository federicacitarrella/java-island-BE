package com.bankIsland.account.rabbit;

public record BackTransferMessage(int status,
                                  int accountOwnerIdTo) {}