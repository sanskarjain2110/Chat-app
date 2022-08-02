package com.stranger.chat.data;

public class Call_Tile_Data {
    public int profilePicId, callActionId, callTypeId;
    public String username, callTime;

    public Call_Tile_Data(int profilePicId, int callActionId, int callTypeId, String username, String callTime) {
        this.profilePicId = profilePicId;
        this.callActionId = callActionId;
        this.callTypeId = callTypeId;
        this.username = username;
        this.callTime = callTime;
    }
}