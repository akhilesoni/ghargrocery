package com.ghargrocery.android;

public class Address {
    String full_name, house_no, area,city,state,pincode,country;

    public Address() {
    }

    public Address(String full_name, String house_no, String area, String city, String state, String pincode, String country) {
        this.full_name = full_name;
        this.house_no = house_no;
        this.area = area;
        this.city = city;
        this.state = state;
        this.pincode = pincode;
        this.country = country;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getHouse_no() {
        return house_no;
    }

    public void setHouse_no(String house_no) {
        this.house_no = house_no;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
