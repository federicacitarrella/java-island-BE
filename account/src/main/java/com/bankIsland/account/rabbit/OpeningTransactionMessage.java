package com.bankIsland.account.rabbit;

public class OpeningTransactionMessage {

    private int accountOwnerId;
    private String accountNumberFrom;
    private String accountNumberTo;
    private double amount;

    public OpeningTransactionMessage() {
    }

    public OpeningTransactionMessage(int accountOwnerId, String accountNumberFrom, String accountNumberTo, double amount) {
        this.accountOwnerId = accountOwnerId;
        this.accountNumberFrom = accountNumberFrom;
        this.accountNumberTo = accountNumberTo;
        this.amount = amount;
    }

    public int getAccountOwnerId() {
        return accountOwnerId;
    }

    public void setAccountOwnerId(int accountOwnerId) {
        this.accountOwnerId = accountOwnerId;
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
