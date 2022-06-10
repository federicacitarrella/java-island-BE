package com.bankIsland.user.rabbit.message;

public record CreationAccountMessage(int accountOwnerId,
                                     String firstName,
                                     String lastName) {}
