package com.example.capstoneproject;

public class RoomContactModel {

    int img;
    float rate;
    String name, address, avalroom, roommembers, price;
    public RoomContactModel(int img, String name, String address, String avalroom, String roommembers, String price, float rate){
        this.img = img;
        this.name = name;
        this.address = address;
        this.avalroom = avalroom;
        this.roommembers = roommembers;
        this.price = price;
        this.rate = rate;
    }

    public int getImg() {
        return img;
    }

    public float getRate() {
        return rate;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getAvalroom() {
        return avalroom;
    }

    public String getRoommembers() {
        return roommembers;
    }

    public String getPrice() {
        return price;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setAvalroom(String avalroom) {
        this.avalroom = avalroom;
    }

    public void setRoommembers(String roommembers) {
        this.roommembers = roommembers;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
