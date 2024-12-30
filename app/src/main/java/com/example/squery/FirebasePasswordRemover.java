package com.example.squery;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebasePasswordRemover {

    private FirebaseDatabase database;
    private DatabaseReference passwordsRef;

    public FirebasePasswordRemover() {
        database = FirebaseDatabase.getInstance();
        passwordsRef = database.getReference("Passwords"); // Ссылка на каталог Passwords
    }

    public void deletePasswordChat(String chatId) {
        DatabaseReference chatRef = passwordsRef.child(chatId); // Ссылка на конкретный чат (например, Chat1)
        chatRef.removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError == null) {
                    System.out.println("Password chat '" + chatId + "' successfully deleted.");
                } else {
                    System.err.println("Error deleting password chat '" + chatId + "': " + databaseError.getMessage());
                }
            }
        });
    }
}

