package com.bankIsland.account.controller.request;

public record AccountCreationRequest (String firstName,
                                      String lastName,
                                      String accountNumber,
                                      double amount) {}