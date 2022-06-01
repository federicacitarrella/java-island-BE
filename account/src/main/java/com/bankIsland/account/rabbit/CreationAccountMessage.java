package com.bankIsland.account.rabbit;

public record CreationAccountMessage (int accountOwnerId,
                                      String firstName,
                                      String lastName) {}
