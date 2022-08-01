package com.stranger.chat.data;

class Chat_Tile_Data {
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

class Status_Grid_Data {
    int profilePicId;
    String username;

    public Status_Grid_Data(int profilePicId, String username) {
        this.profilePicId = profilePicId;
        this.username = username;
    }
}

class Call_Tile_Data {
    int profilePicId, callActionId, callTypeId;
    String username, callTime;

    public Call_Tile_Data(int profilePicId, int callActionId, int callTypeId, String username, String callTime) {
        this.profilePicId = profilePicId;
        this.callActionId = callActionId;
        this.callTypeId = callTypeId;
        this.username = username;
        this.callTime = callTime;
    }
}

class UserData {
    private String username, phoneNumber, password;

    public UserData(String username, String phoneNumber, String password) {
        this.username = username;
        this.phoneNumber = phoneNumber;
        this.password = password;
    }

    public String getUsername() {
        return username;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
