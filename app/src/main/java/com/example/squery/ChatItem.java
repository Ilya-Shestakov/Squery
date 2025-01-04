package com.example.squery;

public class ChatItem {
    private String chatName;

    public ChatItem() {
        // Обязательный конструктор без параметров для Firebase
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