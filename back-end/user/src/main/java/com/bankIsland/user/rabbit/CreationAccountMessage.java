package com.bankIsland.user.rabbit;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class CreationAccountMessage implements Serializable {

    @JsonProperty
    private int accountOwnerId;

    public CreationAccountMessage() {
    }

    public CreationAccountMessage(int accountOwnerId) {
        this.accountOwnerId = accountOwnerId;
    }

    public int getAccountOwnerId() {
        return accountOwnerId;
    }

    public void setAccountOwnerId(int accountOwnerId) {
        this.accountOwnerId = accountOwnerId;
    }
}
