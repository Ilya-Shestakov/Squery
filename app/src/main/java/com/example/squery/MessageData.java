package com.example.squery;

public class MessageData {
    private String message;
    private String time;

    public MessageData(String message, String time) {
        this.message = message;
        this.time = time;
    }
    public String getMessage() {
        return message;
    }

    public String getTime() {
        return time;
    }
}
