package com.example.capstoneproject;

public class MessContactModel {
    private int img = R.drawable.food;
    private String name, address, category, price;
    private float rate = 4;

    //blank constructor is must to implement
    MessContactModel(){}

    public MessContactModel(int img, String mess_name, String address, String category, String price, float rate) {
        this.img = img;
        this.name = mess_name;
        this.address = address;
        this.category = category;
        this.price = price;
        this.rate = rate;
    }

    //getter methods-------------------------------------------------
    public int getImg() {return img;}

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }
    public String getCategory() {
        return category;
    }
    public String getPrice() {
        return price;
    }
    public  float getRate() {
        return rate;
    }

    //setter methods--------------------------------------------------
    public void setImg(int img) {
        this.img = img;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }



}

