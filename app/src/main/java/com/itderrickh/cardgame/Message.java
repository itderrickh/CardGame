package com.itderrickh.cardgame;

public class Message {
    public int userId;
    public String username;
    public String message;

    public Message() {
        this.userId = 0;
        this.username = "";
        this.message = "";
    }

    public Message(int userId, String username, String message) {
        this.userId = userId;
        this.username = username;
        this.message = message;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
