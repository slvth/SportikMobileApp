package com.example.sportikmobileapp.database;

public class UserModel {
    private String Surname;
    private String Name;
    private String Patronymic;
    private String phone;
    private String passport;
    private String email;
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
}
