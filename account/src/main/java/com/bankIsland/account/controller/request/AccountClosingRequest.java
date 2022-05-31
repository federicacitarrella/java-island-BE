package com.bankIsland.account.controller.request;

public class AccountClosingRequest {

    private String accountNumberClosing;
    private String accountNumberTo;

    public AccountClosingRequest() {
    }

    public AccountClosingRequest(String accountNumberClosing, String accountNumberTo) {
        this.accountNumberClosing = accountNumberClosing;
        this.accountNumberTo = accountNumberTo;
    }

    public String getAccountNumberClosing() {
        return accountNumberClosing;
    }

    public void setAccountNumberClosing(String accountNumberClosing) {
        this.accountNumberClosing = accountNumberClosing;
    }

    public String getAccountNumberTo() {
        return accountNumberTo;
    }

    public void setAccountNumberTo(String accountNumberTo) {
        this.accountNumberTo = accountNumberTo;
    }
}
