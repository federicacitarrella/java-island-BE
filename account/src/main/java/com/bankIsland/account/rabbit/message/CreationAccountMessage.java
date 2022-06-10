package com.bankIsland.account.rabbit.message;

public record CreationAccountMessage (int accountOwnerId,
                                      String firstName,
                                      String lastName) {}
