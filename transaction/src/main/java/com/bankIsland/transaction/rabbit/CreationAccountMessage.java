package com.bankIsland.transaction.rabbit;

public record CreationAccountMessage (int accountOwnerId,
                                      String firstName,
                                      String lastName) {}
