package com.example.squery;

import static com.google.firebase.auth.FirebaseAuth.getInstance;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Objects;

public class sing_in extends AppCompatActivity {

    //FIREBASE
    public DatabaseReference refUser;
    public String firebaseUserID = "";

    Button btn_sing_in;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sing_in);


        btn_sing_in = findViewById(R.id.btn_sing_in);

        btn_sing_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });

    }

    public void registerUser() {
        String edit_email = findViewById(R.id.edit_email).toString();
        String edit_login_name = findViewById(R.id.edit_login_name).toString();
        String edit_pass = findViewById(R.id.edit_pass).toString();

        if (edit_login_name.equals("")) {
            Toast.makeText(this, "Enter the name", Toast.LENGTH_SHORT).show();
        } else if (edit_email.equals("")) {
            Toast.makeText(this, "Enter the email", Toast.LENGTH_SHORT).show();
        } else if (edit_pass.equals("")) {
            Toast.makeText(this, "Enter the password", Toast.LENGTH_SHORT).show();
        } else {
            FirebaseAuth mAuth = getInstance();

            mAuth.createUserWithEmailAndPassword(edit_login_name, edit_pass)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            String firebaseUserID = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
                            DatabaseReference refUser;
                            refUser = FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseUserID);

                            HashMap<String, Object> userHashMap = new HashMap<>();
                            userHashMap.put("uid", firebaseUserID);
                            userHashMap.put("username", edit_login_name);
                            userHashMap.put("email", edit_email);
                            userHashMap.put("password", edit_pass);

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

}