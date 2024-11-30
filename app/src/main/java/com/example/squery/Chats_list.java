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
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

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

        welcomeChat = new Dialog(Chats_list.this);

        recycler_view_in_chats_list = findViewById(R.id.recycler_view_in_chats_list);
        btn_add_chat = findViewById(R.id.btn_add_chat);
        username_title_of_chats_list = findViewById(R.id.username_title_of_chats_list);

        recycler_view_in_chats_list.setLayoutManager(new LinearLayoutManager(this));

        createChat = new Dialog(Chats_list.this);

        DataAdapterChats dataAdapter = new DataAdapterChats(this, chats);

        recycler_view_in_chats_list.setAdapter(dataAdapter);

        DatabaseReference myRefChats = database.getReference("Chats");



        String Username = getIntent().getStringExtra("Username");

        username_title_of_chats_list.setText(Username);


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
                    LetsChat(String.valueOf(dataAdapter.chats.get(itemIndex)), myRefChats, Username);
//                    Toast.makeText(Chats_list.this, String.valueOf(dataAdapter.chats.get(itemIndex)), Toast.LENGTH_SHORT).show();
                }
                return false;
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });
    }











    /*-----------------------------------*/
    /*--------------METHODS--------------*/
    /*-----------------------------------*/










    //                                                          ENTRY TO CHAT


    private void LetsChat(String chatname, DatabaseReference myRefChats, String username_title_of_chats_list_from_chat) {

        welcomeChat.setContentView(R.layout.activity_in_the_chat);
        welcomeChat.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        welcomeChat.show();

        editTextCheckPass = welcomeChat.findViewById(R.id.editTextPassWelcome);
        btn_welcome_chat = welcomeChat.findViewById(R.id.btnWelcomeChat);
        btn_delete_chat = welcomeChat.findViewById(R.id.btnDeleteChat);
        titleWelcomeChat = welcomeChat.findViewById(R.id.titleWelcomeChat);

        titleWelcomeChat.setText(chatname);



//                                                              CHECK PASS





        final String[] retPass = new String[1];

        // Получаем ссылку на базу данных Firebase
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

//        // Ссылка на подкаталог Chat в каталоге Passwords

        DatabaseReference passRef = databaseReference.child("Passwords").child(titleWelcomeChat.getText().toString());

//        // Добавляем слушатель для чтения данных
        passRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Проверяем, существует ли элемент
                if (dataSnapshot.exists()) {

                    // Проходим по всем дочерним элементам
                    for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                        // Получаем ключ элемента
                        String key = childSnapshot.getKey();

                        // Получаем значение элемента
                        String chatData = childSnapshot.getValue(String.class);

                        // Обработка данных

                        assert key != null;

                        String chatDataPass = dataSnapshot.child(key).getValue(String.class);

                        retPass[0] = chatDataPass;

                        // Используйте chatData и key в вашем приложении
                    }

                    //                    String key = childSnapshot.getKey();
                    //
                    //                    String chatData = dataSnapshot.child("-O9zPtyfbIRC8muiu86h").getValue(String.class);
                    //                    // Обработка данных

                    //                    Toast.makeText(Chats_list.this, chatData, Toast.LENGTH_SHORT).show();

                    //                    // Используйте chatData в вашем приложении

                } else {
                    // Если элемент не найден
                    Toast.makeText(Chats_list.this, "Data bot found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(Chats_list.this, "Error reading data", Toast.LENGTH_SHORT).show();
            }
        });




        btn_welcome_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editTextCheckPass.getText().toString().equals(retPass[0].toString())){
//                    Toast.makeText(Chats_list.this, "Correct Pass: "+retPass[0], Toast.LENGTH_SHORT).show();

                    welcomeChatBeforeCheck(chatname, username_title_of_chats_list_from_chat);
                } else {
//                    Toast.makeText(Chats_list.this, "Incorrect Pass" + retPass[0], Toast.LENGTH_SHORT).show();
//                    Toast.makeText(Chats_list.this, "Incorrect password", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_delete_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Удаляем элемент с ключом
                myRefChats.child(myRefChats.child(titleWelcomeChat.toString()).getKey()).removeValue();

//                Objects.requireNonNull(myRefChats.child(titleWelcomeChat.toString()).getKey())

                welcomeChat.cancel();
                recreate();
            }
        });

    }



    private void welcomeChatBeforeCheck(String chatname, String username_to_chat) {

        Intent intent = new Intent(this, Chat1.class);
        intent.putExtra("Chatname", chatname);
        intent.putExtra("Username_to_chat", username_to_chat);
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

                DatabaseReference myRefPasswords = database.getReference("Passwords/"+editTextChatName.getText().toString());

                myRefChats.push().setValue(editTextChatName.getText().toString());
                myRefPasswords.push().setValue(editTextChatPass.getText().toString());

                createChat.cancel();

//                Toast.makeText(Chats_list.this, "Developers working for this ...", Toast.LENGTH_SHORT).show();
            }
        });

    }








    @Override
    public void onBackPressed() {

        AuthHelper authHelper = new AuthHelper(this);

        authHelper.logout();
        startActivity(new Intent(this, MainActivity.class));
        finish();

        super.onBackPressed();
    }
}