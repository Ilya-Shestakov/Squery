package com.example.squery;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Objects;

public class sing_in extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference refUser;
    private String firebaseUserID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sing_in);

    }

    public void sing_in_register(View view) {

        mAuth = FirebaseAuth.getInstance();

        EditText edit_email = findViewById(R.id.edit_email);
        EditText edit_login_name = findViewById(R.id.edit_login_name);
        EditText edit_pass = findViewById(R.id.edit_pass);

        if (edit_login_name.equals("")) {
            Toast.makeText(this, "Enter the name", Toast.LENGTH_SHORT).show();
        } else if (edit_email.equals("")) {
            Toast.makeText(this, "Enter the email", Toast.LENGTH_SHORT).show();
        } else if (edit_pass.equals("")) {
            Toast.makeText(this, "Enter the password", Toast.LENGTH_SHORT).show();
        } else {
            mAuth.createUserWithEmailAndPassword(edit_email.getText().toString(), edit_pass.getText().toString())
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            firebaseUserID = mAuth.getCurrentUser().getUid();
                            refUser = FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseUserID);

                            HashMap<String, Object> userHashMap = new HashMap<>();
                            userHashMap.put("uid", firebaseUserID);
                            userHashMap.put("username", edit_login_name.getText().toString());
                            userHashMap.put("email", edit_email.getText().toString());
                            userHashMap.put("password", edit_pass.getText().toString());

                            Toast.makeText(this, "Completed!", Toast.LENGTH_SHORT).show();

                            refUser.updateChildren(userHashMap)
                                    .addOnCompleteListener(task1 -> {
                                        if (task1.isSuccessful()) {
                                            Intent intent = new Intent(this, MainActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    });

                        } else {
                            Toast.makeText(this, Objects.requireNonNull(task.getException()).toString(), Toast.LENGTH_LONG).show();
                        }
                    });
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();

    }
}