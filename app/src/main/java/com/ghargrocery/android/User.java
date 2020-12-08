package com.ghargrocery.android;

public class User {
    public String fname, phone;

    public User() {
    }

    public User(String fname, String phone) {
        this.fname = fname;
        this.phone = phone;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

}
