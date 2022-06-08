package com.bankIsland.transaction.rabbit.message;

public record CreationAccountMessage (int accountOwnerId,
                                      String firstName,
                                      String lastName) {}
