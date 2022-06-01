package com.bankIsland.transaction.rabbit;

public class TransferMessage {

    private String accountNumberFrom;
    private String accountNumberTo;
    private double amount;
    private String cause;
    private int accountOwnerId;

    public TransferMessage() {
    }

    public TransferMessage(String accountNumberFrom, String accountNumberTo, double amount, String cause, int accountOwnerId) {
        this.accountNumberFrom = accountNumberFrom;
        this.accountNumberTo = accountNumberTo;
        this.amount = amount;
        this.cause = cause;
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

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }

    public int getAccountOwnerId() {
        return accountOwnerId;
    }

    public void setAccountOwnerId(int accountOwnerId) {
        this.accountOwnerId = accountOwnerId;
    }
}
