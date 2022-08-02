package com.stranger.chat.data;

public class Chat_Tile_Data {
    int profilePicId, seenStatusId;
    String username, lastText, lastChatTime;

    public Chat_Tile_Data(int profilePicId, int seenStatusId, String username, String lastText, String lastChatTime) {
        this.profilePicId = profilePicId;
        this.seenStatusId = seenStatusId;
        this.username = username;
        this.lastText = lastText;
        this.lastChatTime = lastChatTime;
    }
}