package com.ghargrocery.android.Admin;

public class Order {
    String user_id, order_id,date,state;

    public Order() {
    }



    public Order(String user_id, String order_id, String date,String state) {
        this.user_id = user_id;
        this.order_id = order_id;
        this.date = date;
        this.state = state;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
