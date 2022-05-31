package com.bankIsland.transaction.dto;

import java.util.Date;

public class TransactionDto {

    private int id;
    private int type;
    private String accountNumberFrom;
    private String accountNumberTo;
    private int accountOwnerId;
    private Date date;
    private String cause;
    private double amount;

    public TransactionDto() {
    }

    public TransactionDto(int type, String accountNumberFrom, String accountNumberTo, int accountOwnerId, Date date, String cause, double amount) {
        this.type = type;
        this.accountNumberFrom = accountNumberFrom;
        this.accountNumberTo = accountNumberTo;
        this.accountOwnerId = accountOwnerId;
        this.date = date;
        this.cause = cause;
        this.amount = amount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
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
