package com.example.rent.pojo;

import java.io.Serializable;
import java.util.ArrayList;

public class BookingPoJo implements Serializable {

    String id;
    String adId;
    String ownerId;
    String bookerId;
    String description;
    String price;
    String month;
    ArrayList<String>dates;

    public BookingPoJo(String id, String adId, String ownerId, String bookerId, String description, String price, ArrayList<String> dates) {
        this.id = id;
        this.adId = adId;
        this.ownerId = ownerId;
        this.bookerId = bookerId;
        this.description = description;
        this.price = price;
        this.dates = dates;
    }

    public BookingPoJo(String id, String adId, String ownerId, String bookerId, String description, String price, String month) {
        this.id = id;
        this.adId = adId;
        this.ownerId = ownerId;
        this.bookerId = bookerId;
        this.description = description;
        this.price = price;
        this.month = month;
    }

    public BookingPoJo() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAdId() {
        return adId;
    }

    public void setAdId(String adId) {
        this.adId = adId;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getBookerId() {
        return bookerId;
    }

    public void setBookerId(String bookerId) {
        this.bookerId = bookerId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public ArrayList<String> getDates() {
        return dates;
    }

    public void setDates(ArrayList<String> dates) {
        this.dates = dates;
    }
}
