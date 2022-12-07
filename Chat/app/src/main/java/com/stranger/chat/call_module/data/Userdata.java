package com.stranger.chat.call_module.data;

import java.io.Serializable;

public class Userdata implements Serializable {
    private int profilePicId, callActionId, callTypeId, seenStatusId;
    private String callTime, username, lastText, lastChatTime, userId;

    public Userdata() {
    }

    public Userdata(int profilePicId, String username) {
        this.profilePicId = profilePicId;
        this.username = username;
    }

    public Userdata(int profilePicId, int callActionId, int callTypeId, String username, String callTime) {
        this.profilePicId = profilePicId;
        this.callActionId = callActionId;
        this.callTypeId = callTypeId;
        this.username = username;
        this.callTime = callTime;
    }

    public Userdata(int profilePicId, int seenStatusId, String username, String lastText, String lastChatTime) {
        this.profilePicId = profilePicId;
        this.seenStatusId = seenStatusId;
        this.username = username;
        this.lastText = lastText;
        this.lastChatTime = lastChatTime;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getProfilePicId() {
        return profilePicId;
    }

    public void setProfilePicId(int profilePicId) {
        this.profilePicId = profilePicId;
    }

    public int getCallActionId() {
        return callActionId;
    }

    public void setCallActionId(int callActionId) {
        this.callActionId = callActionId;
    }

    public int getCallTypeId() {
        return callTypeId;
    }

    public void setCallTypeId(int callTypeId) {
        this.callTypeId = callTypeId;
    }

    public String getCallTime() {
        return callTime;
    }

    public void setCallTime(String callTime) {
        this.callTime = callTime;
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