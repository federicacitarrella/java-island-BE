package com.bankIsland.account.rabbit;

public class TransactionMessage {

    private String accountNumber;
    private double amount;
    private int accountOwnerId;

    public TransactionMessage(){}

    public TransactionMessage(String accountNumberFrom, double amount, int accountOwnerId) {
        this.accountNumber = accountNumberFrom;
        this.amount = amount;
        this.accountOwnerId = accountOwnerId;
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

    public int getAccountOwnerId() {
        return accountOwnerId;
    }

    public void setAccountOwnerId(int accountOwnerId) {
        this.accountOwnerId = accountOwnerId;
    }
}
