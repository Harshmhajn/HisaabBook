package com.example.trackexpence.Models;

public class Account {
    private double account_amount;
    private String account_name;

    public Account(double account_amount, String account_name) {
        this.account_amount = account_amount;
        this.account_name = account_name;
    }

    public double getAccount_amount() {
        return account_amount;
    }

    public void setAccount_amount(double account_amount) {
        this.account_amount = account_amount;
    }

    public String getAccount_name() {
        return account_name;
    }

    public void setAccount_name(String account_name) {
        this.account_name = account_name;
    }
}
