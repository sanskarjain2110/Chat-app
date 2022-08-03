package com.stranger.chat.data;

public class Chat_Tile_Data {
    private int profilePicId, seenStatusId;
    private String username, lastText, lastChatTime;

    public Chat_Tile_Data(int profilePicId, int seenStatusId, String username, String lastText, String lastChatTime) {
        this.profilePicId = profilePicId;
        this.seenStatusId = seenStatusId;
        this.username = username;
        this.lastText = lastText;
        this.lastChatTime = lastChatTime;
    }

    public int getProfilePicId() {
        return profilePicId;
    }

    public void setProfilePicId(int profilePicId) {
        this.profilePicId = profilePicId;
    }

    public int getSeenStatusId() {
        return seenStatusId;
    }

    public void setSeenStatusId(int seenStatusId) {
        this.seenStatusId = seenStatusId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getLastText() {
        return lastText;
    }

    public void setLastText(String lastText) {
        this.lastText = lastText;
    }

    public String getLastChatTime() {
        return lastChatTime;
    }

    public void setLastChatTime(String lastChatTime) {
        this.lastChatTime = lastChatTime;
    }
}