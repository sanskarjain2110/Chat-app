package com.stranger.chat.data;

import java.util.Map;

public class Chat_Tile_Data {
    private String lastText, lastSeen, messageId;
    private Map<String, Object> user;

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public Chat_Tile_Data() {
    }

    public Map<String, Object> getUser() {
        return user;
    }

    public void setUsers(Map<String, Object> user) {
        this.user = user;
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

}