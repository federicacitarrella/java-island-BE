package com.bankisland.transaction.controller.request;

public class TransactionRequest {

    private double amount;
    private String accountNumberFrom;
    private String accountNumberTo;

    public TransactionRequest(){}

    public TransactionRequest(double amount, String accountNumberFrom, String accountNumberTo) {
        this.amount = amount;
        this.accountNumberFrom = accountNumberFrom;
        this.accountNumberTo = accountNumberTo;
    }

    public String getAccountNumberFrom() {
        return accountNumberFrom;
    }

    public void setAccountNumberFrom(String accountNumberFrom) {
        this.accountNumberFrom = accountNumberFrom;
    }

    public String getAccountNumberTo() {
        return accountNumberTo;
    }

    public void setAccountNumberTo(String accountNumberTo) {
        this.accountNumberTo = accountNumberTo;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
