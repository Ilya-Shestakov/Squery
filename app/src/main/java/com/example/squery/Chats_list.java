package com.example.squery;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Objects;

public class Chats_list extends AppCompatActivity {

    RecyclerView recycler_view_in_chats_list;
    TextView username_title_of_chats_list, titleWelcomeChat;
    ConstraintLayout btn_add_chat, btn_create_chat, btn_welcome_chat, btn_delete_chat;
    EditText editTextChatName, editTextChatPass, editTextCheckPass;

    FirebaseDatabase database = FirebaseDatabase.getInstance();

    ArrayList<String> chats = new ArrayList<>();

    public Dialog createChat, welcomeChat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chats_list);

        recycler_view_in_chats_list = findViewById(R.id.recycler_view_in_chats_list);
        btn_add_chat = findViewById(R.id.btn_add_chat);
        username_title_of_chats_list = findViewById(R.id.username_title_of_chats_list);

        recycler_view_in_chats_list.setLayoutManager(new LinearLayoutManager(this));

        createChat = new Dialog(Chats_list.this);

        DataAdapterChats dataAdapter = new DataAdapterChats(this, chats);

        recycler_view_in_chats_list.setAdapter(dataAdapter);

        DatabaseReference myRefChats = database.getReference("Chats");

        btn_add_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
    //            if (!editTextMess.getText().toString().trim().isEmpty()) {
      //              if (editTextMess.getText().toString().length() <= 200){
                        createChatDialog(myRefChats);
  //                  }
//                }
            }
        });

        //                                                              UPDATE  CHAT LIST

        myRefChats.addChildEventListener(new ChildEventListener() {
            @Override 
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                String magChat = snapshot.getValue(String.class);

                chats.add(magChat);

                dataAdapter.notifyDataSetChanged();
                recycler_view_in_chats_list.smoothScrollToPosition(chats.size());

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


        //                                                                  TOUCH EVENT


        recycler_view_in_chats_list.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {

            @Override
            public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
            }

            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                View childView = recycler_view_in_chats_list.findChildViewUnder(e.getX(), e.getY());
                if (childView != null) {
                    int position = recycler_view_in_chats_list.getChildAdapterPosition(childView);
                    int itemIndex = dataAdapter.getItemIndex(position);
                    LetsChat(String.valueOf(dataAdapter.chats.get(itemIndex)), myRefChats);
//                    Toast.makeText(Chats_list.this, String.valueOf(dataAdapter.chats.get(itemIndex)), Toast.LENGTH_SHORT).show();
                }
                return false;
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });
    }

    //                                                          ENTRY TO CHAT


    private void LetsChat(String chatname, DatabaseReference myRefChats) {

        welcomeChat = new Dialog(Chats_list.this);

        welcomeChat.setContentView(R.layout.activity_in_the_chat);
        welcomeChat.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        welcomeChat.show();

        editTextCheckPass = welcomeChat.findViewById(R.id.editTextPassWelcome);
        btn_welcome_chat = welcomeChat.findViewById(R.id.btnWelcomeChat);
        btn_delete_chat = welcomeChat.findViewById(R.id.btnDeleteChat);
        titleWelcomeChat = welcomeChat.findViewById(R.id.titleWelcomeChat);

        titleWelcomeChat.setText(chatname);

        btn_welcome_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editTextCheckPass.getText().toString().equals("12345")){
                    welcomeChatBeforeCheck(chatname);
                } else {
                    Toast.makeText(Chats_list.this, "Incorrect password", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_delete_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {




                // Получаем ссылку на каталог chat1
                //DatabaseReference chatRef = FirebaseDatabase.getInstance().getReference("Chats");


                // Удаляем элемент с ключом
                myRefChats.child(myRefChats.child(titleWelcomeChat.toString()).getKey()).removeValue();

//                Objects.requireNonNull(myRefChats.child(titleWelcomeChat.toString()).getKey())

                welcomeChat.cancel();
                recreate();
            }
        });

    }

    private void welcomeChatBeforeCheck(String chatname) {

        Intent intent = new Intent(this, Chat1.class);
        intent.putExtra("Chatname", chatname);
        startActivity(intent);
        finish();

    }





    //                                                         CREATE CHAT



    private void createChatDialog(DatabaseReference myRefChats) {

        createChat.setContentView(R.layout.activity_create_chat);
        createChat.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        createChat.show();

        btn_create_chat = createChat.findViewById(R.id.btnCreateChat);
        editTextChatName = createChat.findViewById(R.id.editTextChatName);
        editTextChatPass = createChat.findViewById(R.id.editTextPass);

        btn_create_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                myRefChats.push().setValue(editTextChatName.getText().toString());

                createChat.cancel();
            }
        });

    }








    @Override
    public void onBackPressed() {

        Intent intent = new Intent(this, sing_up.class);
        startActivity(intent);
        finish();

        super.onBackPressed();
    }
}