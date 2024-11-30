package com.example.squery;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import android.content.Context;
import android.content.SharedPreferences;

import android.content.Intent;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class sing_up extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing_up);
        SharedPreferences token = getSharedPreferences("username", Context.MODE_PRIVATE);
    }

    public void go_sing_in(View view){
        Intent intent = new Intent(this, sing_in.class);
        startActivity(intent);
        finish();
    }

    public void sing_in_of_sing_up(View view) {

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();

        AuthHelper authHelper = new AuthHelper(this);


        String edit_email_sing_up = ((EditText) findViewById(R.id.edit_email_sing_up)).getText().toString();
        String edit_password_sing_up = ((EditText) findViewById(R.id.edit_pass_sing_up)).getText().toString();

        authHelper.saveUser(edit_email_sing_up);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Users");

        myRef.child("username").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        if (edit_email_sing_up.equals("")) {
            Toast.makeText(this, "Enter the email", Toast.LENGTH_SHORT).show();
        } else if (edit_password_sing_up.equals("")) {
            Toast.makeText(this, "Enter the password", Toast.LENGTH_SHORT).show();
        } else {
            mAuth.signInWithEmailAndPassword(edit_email_sing_up, edit_password_sing_up)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {




                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");


                            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()) {
                                        for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                                            // Получаем данные о пользователе
                                            User user = userSnapshot.getValue(User.class);

                                            // Проверяем email
                                            if (user.getEmail().equals(edit_email_sing_up)) {
                                                // Нашли пользователя!
                                                String userId = userSnapshot.getKey(); // Получаем ключ пользователя
//                                                Log.d("Firebase", "Пользователь найден" + userId);


                                                intent(userId);


//                                                Toast.makeText(sing_up.this, "User found: " + userId, Toast.LENGTH_SHORT).show();

                                                //  Здесь вы можете использовать userId для дальнейших действий
                                            }
                                        }
                                    } else {
                                        Toast.makeText(sing_up.this, "User not found", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    Log.w("Firebase", "Ошибка при чтении данных", databaseError.toException());
                                }
                            });



                            //Toast.makeText(this, "Sing up", Toast.LENGTH_LONG).show();

                        } else {
                            Toast.makeText(this, task.getException().toString(), Toast.LENGTH_LONG).show();
                        }
                    });
        }
    }

    public void intent(String putter) {
        Intent intent = new Intent(this, Chats_list.class);
        intent.putExtra("Username", putter);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}