package com.example.rent.pojo;

import java.io.Serializable;
import java.util.HashMap;

public class HotelPoJo implements Serializable {

    String id;
    String name;
    String description;
    String address;
    String email;
    String phone;
    String price;
    HashMap<String,String> downloadUrls;

    public HotelPoJo(String id, String name, String description, String address, String email, String phone, String price, HashMap<String,String> downloadUrls) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.address = address;
        this.email = email;
        this.phone = phone;
        this.price = price;
        this.downloadUrls = downloadUrls;
    }

    public HotelPoJo(String id, String name, String price,HashMap<String,String> downloadUrls) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.downloadUrls = downloadUrls;
    }

    public HotelPoJo(String id, String name, String price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public HotelPoJo() {
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public HashMap<String,String> getDownloadUrls() {
        return downloadUrls;
    }

    public void setDownloadUrls(HashMap<String,String> downloadUrls) {
        this.downloadUrls = downloadUrls;
    }
}
