package com.bankisland.transaction.rabbit;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class CreationAccountMessage implements Serializable {

    @JsonProperty
    private int accountOwnerId;

    @JsonProperty
    private String firstName;

    @JsonProperty
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
