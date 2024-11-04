package com.example.squery;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Objects;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class sing_in extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference refUser, refUserLogin;
    private String firebaseUserID = "";
    private String firebaseUserName = "";
//    FirebaseDatabase database = FirebaseDatabase.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sing_in);

    }

    public void sing_in_register(View view) {

//        mAuth = FirebaseAuth.getInstance();
//
//        EditText edit_username = findViewById(R.id.edit_username);
//        EditText edit_email = findViewById(R.id.edit_email);
//        EditText edit_pass = findViewById(R.id.edit_pass);
//
//        if (edit_email.equals("")) {
//            Toast.makeText(this, "Enter the email", Toast.LENGTH_SHORT).show();
//        } else if (edit_pass.equals("")) {
//            Toast.makeText(this, "Enter the password", Toast.LENGTH_SHORT).show();
//        } else {
//            mAuth.createUserWithEmailAndPassword(edit_email.getText().toString(), edit_pass.getText().toString())
//                    .addOnCompleteListener(task -> {
//                        if (task.isSuccessful()) {
//                            firebaseUserID = mAuth.getCurrentUser().getUid();
////                            firebaseUserName = mAuth.getCurrentUser().getDisplayName();
//
//                            firebaseUserName = edit_username.getText().toString();
//
//                            refUser = FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseUserName);
//
////                            DatabaseReference refUserL = database.getReference("Logins/"+edit_email.getText().toString());
//
//                            //                           refUserL.push().setValue(firebaseUserName);
//
////                            refUserL.push().setValue(edit_username.getText().toString());
//
//                            HashMap<String, Object> userHashMap = new HashMap<>();
////                            userHashMap.put("uid", firebaseUserID);
//                            userHashMap.put("username", firebaseUserName);
//                            userHashMap.put("email", edit_email.getText().toString());
//                            userHashMap.put("password", edit_pass.getText().toString());
//
//
//                            Toast.makeText(this, "Completed!", Toast.LENGTH_SHORT).show();
//
//                            Intent intent = new Intent(this, Chats_list.class);
//                            startActivity(intent);
//                            finish();
//
//                            refUserLogin.push().setValue(firebaseUserName);
//
//                            refUser.updateChildren(userHashMap)
//                                    .addOnCompleteListener(task1 -> {
//                                        if (task1.isSuccessful()) {
//                                            Intent intent1 = new Intent(this, Chats_list.class);
//                                            startActivity(intent1);
//                                            finish();
//                                        }
//                                    });
//                        } else {
//                            Toast.makeText(this, Objects.requireNonNull(task.getException()).toString(), Toast.LENGTH_LONG).show();
//                        }
//                    });
//        }










        mAuth = FirebaseAuth.getInstance();

        EditText edit_username = findViewById(R.id.edit_username);
        EditText edit_email = findViewById(R.id.edit_email);
        EditText edit_pass = findViewById(R.id.edit_pass);

        if (edit_email.equals("")) {
            Toast.makeText(this, "Enter the email", Toast.LENGTH_SHORT).show();
        } else if (edit_pass.equals("")) {
            Toast.makeText(this, "Enter the password", Toast.LENGTH_SHORT).show();
        } else {

//            refUserLogin = FirebaseDatabase.getInstance().getReference().child("Logins").child(edit_email.getText().toString());

//            refUserLogin.push().setValue(edit_username.getText().toString());


            mAuth.createUserWithEmailAndPassword(edit_email.getText().toString(), edit_pass.getText().toString())
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            firebaseUserID = mAuth.getCurrentUser().getUid();
//                            firebaseUserName = mAuth.getCurrentUser().getDisplayName();

                            firebaseUserName = edit_username.getText().toString();

                            refUser = FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseUserName);

//                            Query query = FirebaseDatabase.getInstance().getReference().child("Users").orderByChild(edit_pass.getText().toString()).equalTo(0);
//                            query.addListenerForSingleValueEvent(new ValueEventListener() {
//                                @Override
//                                public void onDataChange(DataSnapshot dataSnapshot) {
//                                    if (dataSnapshot.exists()) {
//                                        // dataSnapshot is the "issue" node with all children with id 0
//                                        for (DataSnapshot issue : dataSnapshot.getChildren()) {
//                                            // do something with the individual "issues"
//
//
//                                            refUserLogin = FirebaseDatabase.getInstance().getReference().child("UserLogins").child(edit_pass.getText().toString());
//
//
//                                            HashMap<String, Object> userHashMap = new HashMap<>();
////                            userHashMap.put("uid", firebaseUserID);
//                                            userHashMap.put("username", firebaseUserName);
//                                            userHashMap.put("email", edit_email.getText().toString());
//                                            userHashMap.put("password", edit_pass.getText().toString());
//
//                                            toast("Completed!");
//
//                                            refUserLogin.push().setValue(firebaseUserName);
//
//                                            refUser.updateChildren(userHashMap)
//                                                    .addOnCompleteListener(task1 -> {
//                                                        if (task1.isSuccessful()) {
//                                                            onBackPressed();
//                                                        }
//                                                    });
//
//                                        }
//                                    }else {
//                                        toast("Password is busy");
//                                    }
//                                }
//                                @Override
//                                public void onCancelled(DatabaseError databaseError) {
//                                }
//                            });



//                            ref.child("Users").orderByChild("password").equalTo(edit_pass.getText().toString()).once("value",snapshot => {
//                                if (snapshot.exists()){
//                                    const userData = snapshot.val();
//                                    console.log("exists!", userData);
//                            }
//                            });







                            refUserLogin = FirebaseDatabase.getInstance().getReference().child("UserLogins").child(edit_pass.getText().toString());


                            HashMap<String, Object> userHashMap = new HashMap<>();
//                            userHashMap.put("uid", firebaseUserID);
                            userHashMap.put("username", firebaseUserName);
                            userHashMap.put("email", edit_email.getText().toString());
                            userHashMap.put("password", edit_pass.getText().toString());

                            //toast("Completed!");

//                            refUserLogin.push().setValue(firebaseUserName);

                            refUser.updateChildren(userHashMap)
                                    .addOnCompleteListener(task1 -> {
                                        if (task1.isSuccessful()) {
                                            onBackPressed();
                                        }
                                    });




//                            HashMap<String, Object> userHashMap = new HashMap<>();
////                            userHashMap.put("uid", firebaseUserID);
//                            userHashMap.put("username", firebaseUserName);
//                            userHashMap.put("email", edit_email.getText().toString());
//                            userHashMap.put("password", edit_pass.getText().toString());
//
//                            Toast.makeText(this, "Completed!", Toast.LENGTH_SHORT).show();
//
//                            refUserLogin.push().setValue(firebaseUserName);
//
//                            refUser.updateChildren(userHashMap)
//                                    .addOnCompleteListener(task1 -> {
//                                        if (task1.isSuccessful()) {
//                                            Intent intent1 = new Intent(this, MainActivity.class);
//                                            startActivity(intent1);
//                                            finish();
//                                        }
//                                    });
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

    public void toast(String text){
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

}