package com.example.rent.PoJo;

public class UserInfoPoJo {

    String userId;
    String name;
    String email;
    String phoneNumber;
    String nidNumber;
    String profileImageUrl;


    public UserInfoPoJo(String userId, String name, String email, String phoneNumber, String nidNumber, String profileImageUrl) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.nidNumber = nidNumber;
        this.profileImageUrl = profileImageUrl;
    }

    public UserInfoPoJo() {
    }

    public String getNidNumber() {
        return nidNumber;
    }

    public void setNidNumber(String nidNumber) {
        this.nidNumber = nidNumber;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }
}
