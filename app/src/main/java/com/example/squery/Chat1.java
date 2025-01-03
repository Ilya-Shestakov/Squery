package com.example.squery;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import androidx.core.app.NotificationCompat;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.NotificationCompat;
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

    TextView chatname_title_of_chat, username_title_of_chat;
    ConstraintLayout btnSendMess, btnDelAll, bottomPanel, btnAddToMyChat;
    EditText editTextMess;
    DBHelper dbHelper;
    RecyclerView mMessagesRecycler;
    public int keyboardHeight = 0;


    ArrayList<String> messages = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat1);

        chatname_title_of_chat = findViewById(R.id.chatname_title_of_chat);
        username_title_of_chat = findViewById(R.id.username_title_of_chat);
        btnDelAll = findViewById(R.id.btn_delete_all);
        btnAddToMyChat = findViewById(R.id.btn_add_to_my_chats);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("ChatsWithMess/" + getIntent().getStringExtra("Chatname"));

        DatabaseReference myRefChats = database.getReference("Chats/" + getIntent().getStringExtra("Chatname"));

        btnAddToMyChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveChat(chatname_title_of_chat.getText().toString());
            }
        });

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


        String chatname = getIntent().getStringExtra("Chatname");

        String username = getIntent().getStringExtra("Username_to_chat");

        username_title_of_chat.setText(username);

        chatname_title_of_chat.setText(chatname);

        DataAdapter dataAdapter = new DataAdapter(this, messages);

        btnSendMess = findViewById(R.id.btnSendMess);
        mMessagesRecycler = findViewById(R.id.resViewMess);
        editTextMess = findViewById(R.id.editTextMess);
        bottomPanel = findViewById(R.id.bottomPanel); // Получаем ссылку на bottomPanel

        mMessagesRecycler.setLayoutManager(new LinearLayoutManager(this));

        mMessagesRecycler.setAdapter(dataAdapter);

        btnSendMess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!editTextMess.getText().toString().trim().isEmpty()) {
                    if (editTextMess.getText().toString().length() <= 500) {
                        //ВАЖНО: Замените  myRef на ваше подключение к базе данных.
                        myRef.push().setValue(username + ":  " + editTextMess.getText().toString()); //Используйте myRef, а не firebaseInstance.
                        editTextMess.setText("");
//                        hideKeyboard();
                    }
                }
            }
        });

        ViewTreeObserver.OnGlobalLayoutListener layoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                adjustRecyclerView();
            }
        };
        View rootView = findViewById(android.R.id.content);
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(layoutListener);



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

    private void saveChat(String chatName) {
        dbHelper = new DBHelper(this);

        if (!dbHelper.chatExists(chatName)){
            // Выполняйте действия, если чата нет в базе
            dbHelper.addChat(chatName);
            Toast.makeText(this, "Чат закреплён", Toast.LENGTH_SHORT).show();
            // Ваш код для создания или добавления
        } else {
            // Выводите сообщение если такой чат есть
            Toast.makeText(this, "Чат с названием " + chatName + " уже закреплён", Toast.LENGTH_SHORT).show();
        }
    }

    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void adjustRecyclerView() {
        Rect rect = new Rect();
        View rootView = findViewById(android.R.id.content);
        rootView.getWindowVisibleDisplayFrame(rect);
        int screenHeight = rootView.getHeight();
        int keyboardHeightNow = screenHeight - rect.bottom;

        if (keyboardHeightNow != keyboardHeight) {
            keyboardHeight = keyboardHeightNow;
            ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) mMessagesRecycler.getLayoutParams();

            if (keyboardHeight > 0) {
                params.bottomToBottom = bottomPanel.getId();
                mMessagesRecycler.setLayoutParams(params);
            } else {
                params.bottomToBottom = bottomPanel.getId();
                mMessagesRecycler.setLayoutParams(params);
            }
            mMessagesRecycler.requestLayout();
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        String username = getIntent().getStringExtra("Username_to_chat");

        Intent intent1 = new Intent(this, Chats_list.class);
        intent1.putExtra("Username", username);
        startActivity(intent1);
        finish();
    }



}