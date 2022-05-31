package com.bankIsland.account.rabbit;

public class ClosingAccountMessage {

    private String accountNumber;

    public ClosingAccountMessage(){}

    public ClosingAccountMessage(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

}
