package com.bankIsland.user.rabbit;

public record CreationAccountMessage(int accountOwnerId,
                                     String firstName,
                                     String lastName) {}
