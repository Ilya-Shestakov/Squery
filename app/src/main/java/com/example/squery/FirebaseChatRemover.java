package com.example.squery;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseChatRemover {

    private FirebaseDatabase database;
    private DatabaseReference chatsWithMessRef;

    public FirebaseChatRemover() {
        database = FirebaseDatabase.getInstance();
        chatsWithMessRef = database.getReference("ChatsWithMess"); // Ссылка на каталог ChatsWithMess
    }

    public void deleteChat(String chatId) {
        DatabaseReference chatRef = chatsWithMessRef.child(chatId); // Ссылка на конкретный чат (например, Chat1)
        chatRef.removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError == null) {
                    System.out.println("Chat '" + chatId + "' successfully deleted.");
                } else {
                    System.err.println("Error deleting chat '" + chatId + "': " + databaseError.getMessage());
                }
            }
        });
    }
}
