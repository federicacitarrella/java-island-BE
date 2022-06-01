package com.bankIsland.account.rabbit;

public class CreationAccountMessage {

    private int accountOwnerId;
    private String firstName;
    private String lastName;

    public CreationAccountMessage() {
    }

    public CreationAccountMessage(int accountOwnerId, String firstName, String lastName) {
        this.accountOwnerId = accountOwnerId;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getAccountOwnerId() {
        return accountOwnerId;
    }

    public void setAccountOwnerId(int accountOwnerId) {
        this.accountOwnerId = accountOwnerId;
    }
}
