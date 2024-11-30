package com.example.squery;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AuthHelper {

    private SharedPreferences sharedPreferences;
    private DatabaseReference databaseReference;

    public AuthHelper(Context context) {
        sharedPreferences = context.getSharedPreferences("my_prefs", Context.MODE_PRIVATE);
        databaseReference = FirebaseDatabase.getInstance().getReference(); //Указывает на корень базы данных
    }

    public void saveUser(String username) {
        sharedPreferences.edit().putString("username", username).apply();
    }

    public boolean isLoggedIn() {
        return sharedPreferences.contains("username");
    }

    public void login(String email, String password, final OnLoginListener listener) {
        DatabaseReference userRef = databaseReference.child(email); // Ссылка на каталог пользователя
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String storedPassword = dataSnapshot.child("password").getValue(String.class);
                    if (password.equals(storedPassword)) {
                        saveUser(email); // Сохраняем username после успешного входа
                        listener.onLoginSuccess();
                    } else {
                        listener.onLoginFailure("Неверный пароль");
                    }
                } else {
                    listener.onLoginFailure("Пользователь не найден");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.onLoginFailure("Ошибка базы данных");
            }
        });
    }

    public void logout() {
        sharedPreferences.edit().remove("username").apply();
    }

    public interface OnLoginListener {
        void onLoginSuccess();
        void onLoginFailure(String message);
    }
}