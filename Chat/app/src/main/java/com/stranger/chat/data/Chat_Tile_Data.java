package com.stranger.chat.data;

public class Chat_Tile_Data {
    private String reciverUsername, lastText, lastSeen, messageId;

    public Chat_Tile_Data() {
    }

    public String getReciverUsername() {
        return reciverUsername;
    }

    public void setReciverUsername(String reciverUsername) {
        this.reciverUsername = reciverUsername;
    }

    public String getLastText() {
        return lastText;
    }

    public void setLastText(String lastText) {
        this.lastText = lastText;
    }

    public String getLastSeen() {
        return lastSeen;
    }

    public void setLastSeen(String lastSeen) {
        this.lastSeen = lastSeen;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }
}