package com.example.sportikmobileapp.database.model;

import java.io.Serializable;

public class AccountModel implements Serializable {
    private int account_id;
    private String login;
    private String password;
    private int role_id;

    public AccountModel() {
    }

    public AccountModel(int account_id, String login, String password, int role_id) {
        this.account_id = account_id;
        this.login = login;
        this.password = password;
        this.role_id = role_id;
    }

    public int getAccount_id() {
        return account_id;
    }

    public void setAccount_id(int account_id) {
        this.account_id = account_id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getRole_id() {
        return role_id;
    }

    public void setRole_id(int role_id) {
        this.role_id = role_id;
    }
}
