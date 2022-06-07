package com.bankIsland.transaction.dto;

import java.time.Instant;

public class TransactionDto {

    private int id;
    private int type;
    private String accountNumberFrom;
    private String accountNumberTo;
    private int accountOwnerIdFrom;
    private int accountOwnerIdTo;
    private Instant date;
    private String cause;
    private double amount;

    public TransactionDto() {
    }

    public TransactionDto(int type, String accountNumberFrom, String accountNumberTo, int accountOwnerIdFrom, int accountOwnerIdTo, Instant date, String cause, double amount) {
        this.type = type;
        this.accountNumberFrom = accountNumberFrom;
        this.accountNumberTo = accountNumberTo;
        this.accountOwnerIdFrom = accountOwnerIdFrom;
        this.accountOwnerIdTo = accountOwnerIdTo;
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

    public Instant getDate() {
        return date;
    }

    public void setDate(Instant date) {
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

    public int getAccountOwnerIdFrom() {
        return accountOwnerIdFrom;
    }

    public void setAccountOwnerIdFrom(int accountOwnerIdFrom) {
        this.accountOwnerIdFrom = accountOwnerIdFrom;
    }

    public int getAccountOwnerIdTo() {
        return accountOwnerIdTo;
    }

    public void setAccountOwnerIdTo(int accountOwnerIdTo) {
        this.accountOwnerIdTo = accountOwnerIdTo;
    }
}
