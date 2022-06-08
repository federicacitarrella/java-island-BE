package com.bankIsland.transaction.controller.request;

public record TransactionRequest (String accountNumber,
                                  int type,
                                  double amount,
                                  String cause) {}

