package com.ghargrocery.android;

public class CartProduct {
    String name, weight, prize, quantity, category,img_url,id;

    public CartProduct(String name, String weight, String prize, String quantity, String category, String img_url, String id) {
        this.name = name;
        this.weight = weight;
        this.prize = prize;
        this.quantity = quantity;
        this.category = category;
        this.img_url = img_url;
        this.id = id;
    }

    public CartProduct() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getPrize() {
        return prize;
    }

    public void setPrize(String prize) {
        this.prize = prize;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
