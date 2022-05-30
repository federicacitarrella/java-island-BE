package com.bankIsland.transaction.controller.request;

public class TransactionRequest {

    private String accountNumber;
    private int type;
    private double amount;

    public TransactionRequest(){}

    public TransactionRequest(String accountNumber, int type, double amount) {
        this.accountNumber = accountNumber;
        this.type = type;
        this.amount = amount;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
