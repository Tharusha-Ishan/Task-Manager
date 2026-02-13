package com.example.simpletaskmanager.models;

import java.util.UUID;

// Simple data class for a Task.
public class Task {

    private String id;
    private String title;
    private String description;
    private boolean isCompleted;
    private long timestamp;

    // Create a new task with a unique ID.
    public Task(String title, String description) {
        this.id = UUID.randomUUID().toString();
        this.title = title;
        this.description = description;
        this.isCompleted = false;
        this.timestamp = System.currentTimeMillis();
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
