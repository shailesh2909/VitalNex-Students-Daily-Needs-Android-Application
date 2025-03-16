package com.example.capstoneproject;

public class User_ProfileContactModel {
    int img;
    String name,sAddress, service;
    public User_ProfileContactModel(int img, String name, String service, String sAddress){
        this.img = img;
        this.name = name;
        this.service = service;
        this.sAddress= sAddress;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setsAddress(String sAddress) {
        this.sAddress = sAddress;
    }

    public int getImg() {
        return img;
    }

    public String getName() {
        return name;
    }

    public String getsAddress() {
        return sAddress;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }
}
