package com.example.sportikmobileapp.database.model;

import com.example.sportikmobileapp.database.model.AccountModel;

import java.io.Serializable;

public class UserModel implements Serializable {
    private int user_id; //ПользовательID
    private String Surname; //Фамилия
    private String Name; //Имя
    private String Patronymic; //Отчество
    private String phone;
    private String passport;
    private String email;
    private int account_id; //Пользователь_системыID
    private AccountModel accountModel;

    public UserModel() {
    }

    public UserModel(String surname, String name, String patronymic, String phone, String passport, String email, AccountModel accountModel) {
        Surname = surname;
        Name = name;
        Patronymic = patronymic;
        this.phone = phone;
        this.passport = passport;
        this.email = email;
        this.accountModel = accountModel;
    }

    public UserModel(int user_id, String surname, String name, String patronymic, String phone, String passport, String email) {
        this.user_id = user_id;
        Surname = surname;
        Name = name;
        Patronymic = patronymic;
        this.phone = phone;
        this.passport = passport;
        this.email = email;
    }

    public String getFio(){
        return Surname+" "+Name+" "+Patronymic;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getSurname() {
        return Surname;
    }

    public void setSurname(String surname) {
        Surname = surname;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPatronymic() {
        return Patronymic;
    }

    public void setPatronymic(String patronymic) {
        Patronymic = patronymic;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassport() {
        return passport;
    }

    public void setPassport(String passport) {
        this.passport = passport;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public AccountModel getAccountModel() {
        return accountModel;
    }

    public void setAccountModel(AccountModel accountModel) {
        this.accountModel = accountModel;
    }

    public int getAccount_id() {
        return account_id;
    }

    public void setAccount_id(int account_id) {
        this.account_id = account_id;
    }
}
