package com.bankIsland.transaction.entity;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "transactions",
        schema = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "type")
    private int type;

    @Column(name = "account_number_from")
    private String accountNumberFrom;

    @Column(name = "account_number_to")
    private String accountNumberTo;

    @Column(name = "account_owner_id_from")
    private int accountOwnerIdFrom;

    @Column(name = "account_owner_id_to")
    private int accountOwnerIdTo;

    @Column(name = "date")
    private Instant date;

    @Column(name = "cause")
    private String cause;

    @Column(name = "amount")
    private double amount;

    public Transaction() {
    }

    public Transaction(int type, String accountNumberFrom, String accountNumberTo, int accountOwnerIdFrom, int accountOwnerIdTo, Instant date, String cause, double amount) {
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

    public void setAccountOwnerIdFrom(int accountOwnerId) {
        this.accountOwnerIdFrom = accountOwnerId;
    }

    public int getAccountOwnerIdTo() {
        return accountOwnerIdTo;
    }

    public void setAccountOwnerIdTo(int accountOwnerIdTo) {
        this.accountOwnerIdTo = accountOwnerIdTo;
    }
}
