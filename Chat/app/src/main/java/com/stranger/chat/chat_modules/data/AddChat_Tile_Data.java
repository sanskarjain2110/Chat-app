package com.stranger.chat.chat_modules.data;

public class AddChat_Tile_Data {
    String username, phoneNumber, email, userId, profileUid;

    public String getProfileUid() {
        return profileUid;
    }

    public void setProfileUid(String profileUid) {
        this.profileUid = profileUid;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}