package com.stranger.chat.data;

public class Sign_upData {
    private String username, email, phoneNumber, messageIds = null;
    private int messageCount = 0;

    public Sign_upData() {
    }

    public int getMessageCount() {
        return messageCount;
    }

    public void setMessageCount(int messageCount) {
        this.messageCount = messageCount;
    }

    public String getMessageIds() {
        return messageIds;
    }

    public void setMessageIds(String messageIds) {
        this.messageIds = messageIds;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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
}
