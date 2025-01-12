package com.example.squery;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.Objects;

public class sing_up extends AppCompatActivity {

    Dialog dialogFogotPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing_up);
        SharedPreferences token = getSharedPreferences("username", Context.MODE_PRIVATE);

        dialogFogotPass = new Dialog(sing_up.this);
    }

    public void go_sing_in(View view){
        Intent intent = new Intent(this, sing_in.class);
        startActivity(intent);
        finish();
    }

    public void sing_in_of_sing_up(View view) {



        String edit_username_sing_up = ((EditText) findViewById(R.id.edit_username_sing_up)).getText().toString();
        String edit_password_sing_up = ((EditText) findViewById(R.id.edit_pass_sing_up)).getText().toString();

        verifyPassword(edit_password_sing_up, edit_username_sing_up);

        AuthHelper authHelper = new AuthHelper(this);

        authHelper.saveUser(edit_username_sing_up);

    }


    public void fogotPass(View view){
        dialogFogotPass.setContentView(R.layout.fogot_password);
        dialogFogotPass.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialogFogotPass.show();

        EditText editTextEmail = dialogFogotPass.findViewById(R.id.fogotPassEditEmail);
        EditText editTextUsername = dialogFogotPass.findViewById(R.id.fogotPassEditUsername);
        ConstraintLayout btnDeleteUser = dialogFogotPass.findViewById(R.id.btnDeleteUser);

        btnDeleteUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteUsername(editTextUsername.getText().toString(), editTextEmail.getText().toString());
            }
        });

    }



    public void DeleteUsername(String username, String email) {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference userRef = databaseReference.child("Users").child(username);

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String storedHashedUsername = dataSnapshot.child("username").getValue(String.class);
                    String storedHashedEmail = dataSnapshot.child("email").getValue(String.class);
                    if (storedHashedUsername != null && storedHashedEmail != null) {
                        boolean EmailMatches = verifyHashedEmail(email, storedHashedEmail);
                        boolean UsernameMatches = verifyHashedUsername(username, storedHashedUsername);
                        if (EmailMatches && UsernameMatches) {
                            Toast.makeText(sing_up.this, "Аккаунт удалён", Toast.LENGTH_SHORT).show();
                            dialogFogotPass.cancel();
                            userRef.removeValue();
                        } else {
                            Toast.makeText(sing_up.this, "email или username неверный, попробуйте ещё раз", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(sing_up.this, "Пароль не найден для пользователя " + username, Toast.LENGTH_SHORT).show();
                    }
                } else {

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.err.println("Ошибка доступа: " + databaseError.getMessage());
            }
        });
    }



    public void verifyPassword(String passwordEditText, String username) {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference userRef = databaseReference.child("Users").child(username);

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String storedHashedPassword = dataSnapshot.child("password").getValue(String.class);
                    if (storedHashedPassword != null) {
                        boolean passwordMatches = verifyHashedPassword(passwordEditText, storedHashedPassword);
                        if (passwordMatches) {
//                            Toast.makeText(sing_up.this, "Пароль верный", Toast.LENGTH_SHORT).show();
                            intent(username);
                        } else {
                            Toast.makeText(sing_up.this, "Пароль неверный, попробуйте ещё раз", Toast.LENGTH_SHORT).show();
                            // Действия после неудачной проверки
                        }
                    } else {
                        Toast.makeText(sing_up.this, "Пароль не найден для пользователя " + username, Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.err.println("Ошибка доступа: " + databaseError.getMessage());
            }
        });
    }



    private boolean verifyHashedPassword(String enteredPassword, String storedHashedPassword) {
        return Objects.equals(enteredPassword, storedHashedPassword);
    }

    private boolean verifyHashedEmail(String enteredEmail, String storedHashedEmail) {
        return Objects.equals(enteredEmail, storedHashedEmail);
    }
    private boolean verifyHashedUsername(String enteredUsername, String storedHashedUsername) {
        return Objects.equals(enteredUsername, storedHashedUsername);
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

        Intent intent = new Intent(this, sing_in.class);
        startActivity(intent);
        finish();
    }
}