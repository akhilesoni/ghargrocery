package com.ghargrocery.android;

public class Category {
    String icon_url,name;

    public Category() {
    }

    public Category(String image_url, String name) {
        this.icon_url = image_url;
        this.name = name;
    }

    public String getIcon_url() {
        return icon_url;
    }

    public void setIcon_url(String image_url) {
        this.icon_url = image_url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

