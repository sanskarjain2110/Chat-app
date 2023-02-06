package com.stranger.chat.notes_modules.data;

public class Task_Tile_Data {
    String title, taskId, description;
    boolean status;

    public boolean isStatus() {
        return status;
    }

    public String getTaskId() {
        return taskId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

