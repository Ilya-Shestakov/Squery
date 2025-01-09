package com.example.squery;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.icu.text.SimpleDateFormat;
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
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class Chat1 extends AppCompatActivity {

    TextView chatname_title_of_chat, username_title_of_chat;
    ConstraintLayout btnSendMess, bottomPanel, btn_show_sett, btn_add_to_my_chats_in_dial, btn_show_info_about_chat_in_dial, btn_clear_chat_in_dial, btn_show_qr, main;
    EditText editTextMess;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    Dialog dialogInfo;
//    DBHelper dbHelper;
    RecyclerView mMessagesRecycler;
    public int keyboardHeight = 0;

    ArrayList<MessageData> messages = new ArrayList<>();


    //FirebaseChatManager chatManager;

    public static boolean isChatActivityActive = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat1);

        chatname_title_of_chat = findViewById(R.id.chatname_title_of_chat);
        username_title_of_chat = findViewById(R.id.username_title_of_chat);
        main = findViewById(R.id.main);


        btn_show_sett = findViewById(R.id.btn_show_sett);

        dialogInfo = new Dialog(this);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("ChatsWithMess/" + getIntent().getStringExtra("Chatname"));

        DatabaseReference myRefChats = database.getReference("Chats/" + getIntent().getStringExtra("Chatname"));


        String chatpass = getIntent().getStringExtra("Chatpass");
        String chatname = getIntent().getStringExtra("Chatname");

//        chatManager = new FirebaseChatManager(this, chatname);
//        MyApplication application = (MyApplication) getApplication();
//        if (application.isAppInForeground()){
//
//        }else{
//            getLifecycle().addObserver(chatManager);
//        }
//
//        chatManager.startListeningForNewMessages(this, chatname);

        final String[] chatPass = {""};

        getSinglePassword(chatname, new OnPasswordReceivedListener() {
            @Override
            public void onPasswordReceived(String password) {
                if(password != null){
                    // Используем полученный пароль
                    chatPass[0] = password;
                } else {
                    chatPass[0] = "";
                }
            }
            @Override
            public void onError(String errorMessage) {

            }
        });

        btn_show_sett.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                method_add_to_my_chat(chatPass[0] ,myRef ,chatname_title_of_chat.getText().toString(), username_title_of_chat.getText().toString());
            }
        });


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

        String currentTime = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());

        btnSendMess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!editTextMess.getText().toString().trim().isEmpty()) {
                    if (editTextMess.getText().toString().length() <= 500) {
                        //ВАЖНО: Замените  myRef на ваше подключение к базе данных.
                        myRef.push().setValue(username + "("+currentTime+"):" + editTextMess.getText().toString()); //Используйте myRef, а не firebaseInstance.
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

                messages.add(new MessageData(mag, ""));

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



        btn_add_to_my_chats_in_dial = findViewById(R.id.btn_add_to_my_chats_in_dial);

        btn_add_to_my_chats_in_dial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveChat(chatname, username_title_of_chat.getText().toString());
                dialogInfo.cancel();
            }
        });





    }


    private boolean isActivityActive() {
        return !isFinishing() && !isDestroyed();
    }


    interface OnPasswordReceivedListener {
        void onPasswordReceived(String password);
        void onError(String errorMessage);
    }


    private void getSinglePassword(String chatname, final OnPasswordReceivedListener listener) {
        DatabaseReference passwordsRef = database.getReference("Passwords/" + chatname);

        passwordsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                        if (childSnapshot.getValue() != null) {
                            String password = childSnapshot.getValue(String.class);
                            listener.onPasswordReceived(password);
                            return;
                        }
                    }
                }
                listener.onPasswordReceived(null);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                listener.onError(error.getMessage());
            }
        });

    }




//    private String getSinglePassword(String chatname) {
//        DatabaseReference passwordsRef = database.getReference("Passwords/" + chatname);
//        final String[] chatpassFromDB = {null};
//        passwordsRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (snapshot.exists()) {
//                    for (DataSnapshot childSnapshot : snapshot.getChildren()) {
//                        if (childSnapshot.getValue() != null) {
//                            toast("TEST");
//                            chatpassFromDB[0] = childSnapshot.getValue(String.class);
//                            break;
//                        }
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//            }
//
//        });
//
//        return chatpassFromDB[0];
//
//    }









    //                                                      SAVE CHAT






    public void method_add_to_my_chat(String chatpass, DatabaseReference myRef, String chatname, String username)
    {


        dialogInfo.setContentView(R.layout.activity_chat_info);
        dialogInfo.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialogInfo.show();

        btn_show_info_about_chat_in_dial = dialogInfo.findViewById(R.id.btn_show_info_about_chat_in_dial);
        btn_clear_chat_in_dial = dialogInfo.findViewById(R.id.btn_clear_chat_in_dial);
        btn_show_qr = dialogInfo.findViewById(R.id.btn_show_qr);

        btn_show_qr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toast("Работаем над этим...");
            }
        });

        btn_clear_chat_in_dial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myRef.removeValue();
                dialogInfo.cancel();
                recreate();
            }
        });

        btn_show_info_about_chat_in_dial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toast("Название: " + chatname + "\n" + "Пароль: " + chatpass);

            }
        });

    }







    private void saveChat(String chatName, String userName) {

//        DatabaseReference myRefPasswords = database.getReference("PinnedChats/" + userName);
//
//        myRefPasswords.push().setValue(chatName);

        DatabaseReference myRefPasswords = database.getReference("PinnedChats/" + userName);

        myRefPasswords.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(chatName)) {
                    DatabaseReference helper2 = database.getReference("PinnedChats/" + userName + "/" + chatName);
                    helper2.removeValue();
                    toast("Чат откреплён");
                } else {
                    DatabaseReference helper = database.getReference("PinnedChats/" + userName + "/" + chatName);
                    helper.push().setValue("null");
                    toast("Чат закреплён");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Ошибка при проверке, выводим сообщение
                toast("Ошибка проверки на индивидуальность");
            }
        });

//        parentRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                DataSnapshot childSnapshot = snapshot.child(userName);
//                boolean exists = childSnapshot.exists();
//
//                DatabaseReference myRefPasswords = database.getReference("PinnedChats/" + userName);
//                if (snapshot.hasChild(userName)) {
//                    // Если подкаталога нет - создаем и записываем значение
//                    toast("Чат уже закреплён");
//                } else {
//                    myRefPasswords.push().setValue(chatName);
//                    toast("Чат закреплён");
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                toast("System Error");
//            }
//        });






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
        String chatname = getIntent().getStringExtra("Chatname");

        Intent intent1 = new Intent(this, Chats_list.class);
        intent1.putExtra("Username", username);
        startActivity(intent1);
        finish();

        isChatActivityActive = false;


//        FirebaseChatManager chatManager = new FirebaseChatManager(Chat1.this, chatname);
//        chatManager.stopListeningForNewMessages();
    }

    public void toast(String text){
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

}