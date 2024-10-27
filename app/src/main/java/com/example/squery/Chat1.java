package com.example.squery;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Objects;

public class Chat1 extends AppCompatActivity {

    TextView username_title_of_chat;

    ConstraintLayout btnSendMess, btnDelAll;
    EditText editTextMess;
    RecyclerView mMessagesRecycler;

    ArrayList<String> messages = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chat1);

        username_title_of_chat = findViewById(R.id.username_title_of_chat);
        btnDelAll = findViewById(R.id.btn_delete_all);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("ChatsWithMess/" + getIntent().getStringExtra("Chatname"));

        DatabaseReference myRefChats = database.getReference("Chats/" + getIntent().getStringExtra("Chatname"));

        btnDelAll.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UnsafeIntentLaunch")
            @Override
            public void onClick(View view) {

                myRef.removeValue();

                //myRefChats.child(username_title_of_chat.toString()).removeValue();

//                Toast.makeText(Chat1.this, myRefChats.toString(), Toast.LENGTH_SHORT).show();

                //backPressHand();
                recreate();
            }
        });

        String username = getIntent().getStringExtra("Chatname");

        username_title_of_chat.setText(username);

        btnSendMess = findViewById(R.id.btnSendMess);
        editTextMess = findViewById(R.id.editTextMess);
        mMessagesRecycler = findViewById(R.id.resViewMess);

        mMessagesRecycler.setLayoutManager(new LinearLayoutManager(this));

        DataAdapter dataAdapter = new DataAdapter(this, messages);

        mMessagesRecycler.setAdapter(dataAdapter);

        btnSendMess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!editTextMess.getText().toString().trim().isEmpty()) {
                    if (editTextMess.getText().toString().length() <= 500) {
                        myRef.push().setValue("User 1" + ":  " + editTextMess.getText().toString());
                        editTextMess.setText("");
                    }
                }
            }
        });



        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                String mag = snapshot.getValue(String.class);

                messages.add(mag);

                dataAdapter.notifyDataSetChanged();
                mMessagesRecycler.smoothScrollToPosition(messages.size());

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
          }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    public void backPressHand(){
        Intent intent1 = new Intent(this, Chats_list.class);
        startActivity(intent1);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent1 = new Intent(this, Chats_list.class);
        startActivity(intent1);
        finish();

    }
}