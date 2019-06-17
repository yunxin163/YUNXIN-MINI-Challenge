package com.tzutalin.dlibtest;

import java.util.List;

public class Mine  {
    private int imageID=0;
    private String name=null;
    private String password=null;
    private String phone=null;
    private List<String> rolePhone=null;
    private String image=null;
    private String signature = null;
    private String token =null;

    public Mine(int imageID, String name, String password, String phone, List<String> rolePhone) {
        this.imageID = imageID;
        this.name = name;
        this.password = password;
        this.phone = phone;
        this.rolePhone = rolePhone;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Mine() {
    }

    public static Mine mine = new Mine();
    public static Mine getInstance(){
        return mine;
    }
    public Mine(String name, String phone, String image) {
        this.image = image;
        this.name = name;
        this.phone = phone;
    }

    public String getToken() {
        return token;
    }

    public Mine(String name, String phone, String image, String signature, String token) {
        this.name = name;
        this.phone = phone;
        this.image = image;
        this.signature = signature;
        this.token=token;
    }

    public String getImage() {
        return image;
    }

    public String getSignature() {
        return signature;
    }

    public List<String> getRolePhone() {
        return rolePhone;
    }

    public void setRolePhone(List<String> rolePhone) {
        this.rolePhone = rolePhone;
    }

    public Mine(int imageID, String name, String password, String phone) {
        this.imageID = imageID;
        this.name = name;
        this.password = password;
        this.phone = phone;
    }

    public int getImageID() {
        return imageID;
    }

    public void setImageID(int imageID) {
        this.imageID = imageID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
