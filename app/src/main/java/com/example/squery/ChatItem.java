package com.example.squery;

public class ChatItem {
    private String chatName;

    public ChatItem() {
    }

    public ChatItem(String chatName) {
        this.chatName = chatName;
    }

    public String getChatName() {
        return chatName;
    }

    public void setChatName(String chatName) {
        this.chatName = chatName;
    }
}