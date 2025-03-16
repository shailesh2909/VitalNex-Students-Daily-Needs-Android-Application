package com.example.capstoneproject;

public class MedicalContactModel {
   private int img = R.drawable.pills;
   private int cerimg = R.drawable.pills;
    public String name, address, cat,drname;
    private float rat = 3;
    MedicalContactModel(){}
    public MedicalContactModel(int img, String medical_name, String address, String cat, String drname, float rat){
        this.img = img;
        this.name = medical_name;
        this.address = address;
        this.cat = cat;
        this.drname = drname;
        this.rat = rat;
    }

    public int getImg() {
        return img;
    }

    public int getCerimg() {
        return cerimg;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getCat() {
        return cat;
    }

    public String getDrname() {
        return drname;
    }

    public float getRat() {
        return rat;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public void setCerimg(int cerimg) {
        this.cerimg = cerimg;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setCat(String cat) {
        this.cat = cat;
    }

    public void setDrname(String drname) {
        this.drname = drname;
    }

    public void setRat(float rat) {
        this.rat = rat;
    }


}