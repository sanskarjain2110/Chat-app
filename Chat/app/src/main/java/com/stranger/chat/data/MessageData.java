package com.stranger.chat.data;

public class MessageData {
    String username, timeStamp, messageArea;

    MessageData() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getMessageArea() {
        return messageArea;
    }

    public void setMessageArea(String messageArea) {
        this.messageArea = messageArea;
    }
}