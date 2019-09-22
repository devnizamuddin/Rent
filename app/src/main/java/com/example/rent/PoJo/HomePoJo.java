package com.example.rent.PoJo;

import java.io.Serializable;
import java.util.ArrayList;

public class HomePoJo implements Serializable {

    String id;
    String userId;
    String area;
    String description;
    String priceType;
    String amount;
    ArrayList<String> downloadUrls;
    MyLocation myLocation;

    public HomePoJo() {
    }

    public HomePoJo(String id, String userId, String area, String description, String priceType, String amount, ArrayList<String> downloadUrls) {
        this.id = id;
        this.userId = userId;
        this.area = area;
        this.description = description;
        this.priceType = priceType;
        this.amount = amount;
        this.downloadUrls = downloadUrls;
    }


    public HomePoJo(String id, String userId, String area, String description, String priceType, String amount, ArrayList<String> downloadUrls, MyLocation myLocation) {
        this.id = id;
        this.userId = userId;
        this.area = area;
        this.description = description;
        this.priceType = priceType;
        this.amount = amount;
        this.downloadUrls = downloadUrls;
        this.myLocation = myLocation;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPriceType() {
        return priceType;
    }

    public void setPriceType(String priceType) {
        this.priceType = priceType;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public ArrayList<String> getDownloadUrls() {
        return downloadUrls;
    }

    public void setDownloadUrls(ArrayList<String> downloadUrls) {
        this.downloadUrls = downloadUrls;
    }

    public MyLocation getMyLocation() {
        return myLocation;
    }

    public void setMyLocation(MyLocation myLocation) {
        this.myLocation = myLocation;
    }
}
