package com.bankIsland.transaction.controller.request;

public class TransferRequest {

    private String accountNumberFrom;
    private String accountNumberTo;
    private int type;
    private double amount;
    private String cause;

    public TransferRequest(){}

    public TransferRequest(String accountNumberFrom, String accountNumberTo, int type, double amount, String cause) {
        this.accountNumberFrom = accountNumberFrom;
        this.accountNumberTo = accountNumberTo;
        this.type = type;
        this.amount = amount;
        this.cause = cause;
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
