package com.example.squery;

public class ChatItem {
    private int id; // Добавляем ID
    private String chatName;

    public ChatItem(int id, String chatName) {
        this.id = id;
        this.chatName = chatName;
    }

    public int getId() {
        return id;
    }

    public String getChatName() {
        return chatName;
    }
}
