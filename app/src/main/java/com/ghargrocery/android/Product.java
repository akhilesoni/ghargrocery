package com.ghargrocery.android;

public class Product {
    private String product_name, product_prize,product_weight,product_img_url,product_category,product_id;
    private Boolean in_featured;

    public Product() {
    }

    public Product(String product_name, String product_prize, String product_weight, String product_img_url,String product_category,String product_id,Boolean in_featured) {
        this.product_name = product_name;
        this.product_prize = product_prize;
        this.product_weight = product_weight;
        this.product_img_url = product_img_url;
        this.product_category = product_category;
        this.product_id  = product_id;
        this.in_featured = in_featured;

    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_prize() {
        return product_prize;
    }

    public void setProduct_prize(String product_prize) {
        this.product_prize = product_prize;
    }

    public String getProduct_weight() {
        return product_weight;
    }

    public void setProduct_weight(String product_weight) {
        this.product_weight = product_weight;
    }

    public String getProduct_img_url() {
        return product_img_url;
    }

    public void setProduct_img_url(String product_img_url) {
        this.product_img_url = product_img_url;
    }

    public String getProduct_category() {
        return product_category;
    }

    public void setProduct_category(String product_catagory) {
        this.product_category = product_catagory;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public Boolean getIn_featured() {
        return in_featured;
    }

    public void setIn_featured(Boolean in_featured) {
        this.in_featured = in_featured;
    }
}

