package com.example.squery;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class Chats_list extends AppCompatActivity {

    RecyclerView recycler_view_in_chats_list, recyclerViewMyChats;
    TextView username_title_of_chats_list, titleWelcomeChat;
    ConstraintLayout btn_add_chat, btn_create_chat,btn_find_chat, btn_welcome_chat, btn_del_chat_in_pin, btn_unpin_chat_in_pin, btn_letsGo_to_chat_in_pin, btn_delete_chat, btn_find_chat_in_wind, btn_my_chats;
    EditText editTextChatName, editTextChatPass, editTextCheckPass, searchEditText;
    DataAdapterChats dataAdapter;

    static final int DOUBLE_BACK_PRESS_DELAY = 2000; // Время в миллисекундах
    long lastBackPressedTime = 0;

    private static final int DOUBLE_CLICK_INTERVAL = 1000;
    private long lastClickTime = 0;
    private int lastClickPosition = -1;
    private boolean isMyChatsInitialized = false;

    private static final int DOUBLE_CLICK_INTERVAL_FOR_FIND = 1000;
    private long lastClickTimeFind = 0;
    private int lastClickPositionFind = -1;
    private boolean isMyChatsInitializedFind = false;

    FirebaseDatabase database = FirebaseDatabase.getInstance();

    ArrayList<String> chats = new ArrayList<>();

    List<ChatItem> chatItems1 = new ArrayList<>();


//    ArrayList<ChatItem> MyChats = new ArrayList<>();


    DatabaseReference myRefChats = database.getReference("Chats");
    DatabaseReference mySavedChatsRef = database.getReference("PinnedChats");

    private ActivityResultLauncher<String> requestPermissionLauncher;

    public Dialog createChat, welcomeChat, findChat, myChatsDial, edit_pin_chat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chats_list);





        //                                  MESSAGE

        requestPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                isGranted -> {
                    if (isGranted) {
                        Toast.makeText(this, "Разрешение на уведомления получено", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Разрешение на уведомления отклонено", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        // Запрос разрешения, если это необходимо
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
            }
        }








        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                handleBackButtonPress();
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);

        welcomeChat = new Dialog(Chats_list.this);
        findChat = new Dialog(Chats_list.this);
        createChat = new Dialog(Chats_list.this);
        myChatsDial = new Dialog(Chats_list.this);
        edit_pin_chat = new Dialog(Chats_list.this);

        btn_add_chat = findViewById(R.id.btn_add_chat);
        btn_find_chat = findViewById(R.id.btn_find_chat);

        String Username = getIntent().getStringExtra("Username");

        btn_find_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                method_find_chat(getIntent().getStringExtra("Username"));

            }
        });

        username_title_of_chats_list = findViewById(R.id.username_title_of_chats_list);

        username_title_of_chats_list.setText(getIntent().getStringExtra("Username"));









        //                                  FIX CHATS


        recyclerViewMyChats = findViewById(R.id.recyclerViewMyChats);

        DatabaseReference myRefMyChats = database.getReference("PinnedChats/" + getIntent().getStringExtra("Username"));

        ChatItemAdapter adapter = new ChatItemAdapter(chatItems1);

        recyclerViewMyChats.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewMyChats.setAdapter(adapter);

        myRefMyChats.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chatItems1.clear();
                for (DataSnapshot chatSnapshot : snapshot.getChildren()) {
                    String chatName = chatSnapshot.getKey();
                    if (chatName != null) {
                        ChatItem chatItem2 = new ChatItem(chatName);
                        chatItems1.add(chatItem2);
                    }
                }
                adapter.setChatItems(chatItems1);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        if(!isMyChatsInitialized){

            recyclerViewMyChats.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
                @Override
                public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                    View childView = rv.findChildViewUnder(e.getX(), e.getY());
                    if (childView != null) {
                        int positionOfMyChats = rv.getChildAdapterPosition(childView);
                        long currentTime = SystemClock.elapsedRealtime();
                        ChatItem item = adapter.getItemAdapter(positionOfMyChats);

                        if (e.getActionMasked() == MotionEvent.ACTION_UP) {
                            if (positionOfMyChats == lastClickPosition && currentTime - lastClickTime <= DOUBLE_CLICK_INTERVAL) {
                                if (item != null) {
                                    LetsChatOfPin(item.getChatName(), getIntent().getStringExtra("Username"));

                                    lastClickTime = 0;
                                    lastClickPosition = -1;
                                    return false;
                                } else {
                                    Toast.makeText(Chats_list.this, "Не удалось войти в чат", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                lastClickTime = currentTime;
                                lastClickPosition = positionOfMyChats;
                                toast("Нажимте ещё раз что бы зайти в чат");
                            }
                            return false;
                        }
                    }
                    return false;
                }

                @Override
                public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {}
                @Override
                public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {}
            });
            isMyChatsInitialized = true;
        }








        btn_add_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createChatDialog(myRefChats);
            }
        });

    }











    /*-----------------------------------*/
    /*--------------METHODS--------------*/
    /*-----------------------------------*/








    //                                      SENDING MESSAGING


    private void sendMessFirstChat() {
        NotificationHelper notificationHelper = new NotificationHelper(this);
        notificationHelper.sendNotification("Squery", "Новое сообщение");
    }











    //                                      CALLBACK



    private void handleBackButtonPress() {
        if (System.currentTimeMillis() - lastBackPressedTime > DOUBLE_BACK_PRESS_DELAY) {
            Toast.makeText(this, "Нажмите ещё раз для выхода из аккаунта", Toast.LENGTH_SHORT).show();
            lastBackPressedTime = System.currentTimeMillis();
        } else {
            AuthHelper authHelper = new AuthHelper(this);

            authHelper.logout();
            startActivity(new Intent(this, MainActivity.class));
            finish();// Выход

            toast("Вы вышли из учётной записи");
        }
    }




    //                                                          ENTRY TO CHAT




    public void LetsChatOfPin(String chatname, String username){

        edit_pin_chat.setContentView(R.layout.activity_edit_chat_for_my_chats);
        edit_pin_chat.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        edit_pin_chat.show();

        btn_del_chat_in_pin = edit_pin_chat.findViewById(R.id.btn_del_chat_in_pin);
        btn_letsGo_to_chat_in_pin = edit_pin_chat.findViewById(R.id.btn_letsGo_to_chat_in_pin);
        btn_unpin_chat_in_pin = edit_pin_chat.findViewById(R.id.btn_unpin_chat_in_pin);

        btn_letsGo_to_chat_in_pin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent(chatname, username);
            }
        });

        btn_del_chat_in_pin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebasePasswordRemover removerPass = new FirebasePasswordRemover();
                removerPass.deletePasswordChat(chatname);

                deleteChat(myRefChats, mySavedChatsRef, chatname, getIntent().getStringExtra("Username"));

                edit_pin_chat.cancel();

                recreate();

            }
        });

        DatabaseReference unPinChatRef = database.getReference("PinnedChats").child(getIntent().getStringExtra("Username")).child(chatname);

        btn_unpin_chat_in_pin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                unPinChatRef.removeValue();

            }
        });
    }







    private void LetsChat(String chatname, DatabaseReference myRefChats, String username_title_of_chats_list_from_chat, DatabaseReference mySavedChatsRef) {

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

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference passRef = databaseReference.child("Passwords").child(titleWelcomeChat.getText().toString());

        passRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                        String key = childSnapshot.getKey();
                        String chatData = childSnapshot.getValue(String.class);

                        assert key != null;

                        String chatDataPass = dataSnapshot.child(key).getValue(String.class);

                        retPass[0] = chatDataPass;

                    }

                } else {
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
                    welcomeChatBeforeCheck(chatname, username_title_of_chats_list_from_chat, editTextCheckPass.getText().toString());
                } else {
                    Toast.makeText(Chats_list.this, "Неверный пароль", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_delete_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (editTextCheckPass.getText().toString().equals(retPass[0].toString())){

                    FirebasePasswordRemover removerPass = new FirebasePasswordRemover();
                    removerPass.deletePasswordChat(chatname);

                    deleteChat(myRefChats, mySavedChatsRef, chatname, getIntent().getStringExtra("Username"));

                    welcomeChat.cancel();
                    findChat.cancel();

                    recreate();

                } else {
                    Toast.makeText(Chats_list.this, "Неверный пароль", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    public void deleteChat(DatabaseReference chatsRef, DatabaseReference MySavedChatRef, String targetValue, String username){

        MySavedChatRef.child(username).child(targetValue).removeValue();

        Query query = chatsRef.orderByValue().equalTo(targetValue);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String key = snapshot.getKey();

                        // Правильный код с использованием CompletionListener
                        chatsRef.child(key).removeValue(new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                if (databaseError == null) {
                                    System.out.println("Data with key '" + key + "' successfully deleted");
                                } else {
                                    System.err.println("Error deleting data: " + databaseError.getMessage());
                                }
                            }
                        });

//                        dbHelper.deleteChatByName(chatName);

                    }
                } else {
                    System.out.println("No data found with the value: " + targetValue);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.err.println("Query cancelled: " + databaseError.getMessage());
            }
        });
    }


    private void welcomeChatBeforeCheck(String chatname, String username_to_chat, String password) {

        Intent intent = new Intent(this, Chat1.class);
        intent.putExtra("Chatname", chatname);
        intent.putExtra("Chatpass", password);
        intent.putExtra("Username_to_chat", username_to_chat);
        startActivity(intent);
        finish();

    }





    //                                                        FIND CHAT



    public void method_find_chat(String Username){
        findChat.setContentView(R.layout.activity_dialog_find_chat);
        findChat.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        findChat.show();

        recycler_view_in_chats_list = findChat.findViewById(R.id.recycler_view_in_chats_list);

        chats = getChatTitles();

        dataAdapter = new DataAdapterChats(this, chats);

        recycler_view_in_chats_list.setLayoutManager(new LinearLayoutManager(this));
        recycler_view_in_chats_list.setAdapter(dataAdapter);


        recycler_view_in_chats_list.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {

            @Override
            public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
            }

            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                View childView = recycler_view_in_chats_list.findChildViewUnder(e.getX(), e.getY());
                if (childView != null) {

                    int position = recycler_view_in_chats_list.getChildAdapterPosition(childView);
                    int itemIndex = DataAdapterChats.getItemIndexFind(position);

                    LetsChat(String.valueOf(dataAdapter.filteredChats.get(itemIndex)), myRefChats, Username, mySavedChatsRef);

                    findChat.cancel();
                }
                return false;
            }
            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }


        });

        searchEditText = findChat.findViewById(R.id.searchEditText);
        btn_find_chat_in_wind = findChat.findViewById(R.id.find_in_wind);

        btn_find_chat_in_wind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleSearchAndDismiss(searchEditText);
            }
        });
    }




    private void handleSearchAndDismiss(EditText searchEditTextp2) {
        String searchText = searchEditTextp2.getText().toString();
        filterAndUpdateRecyclerView(searchText);
    }


    private void filterAndUpdateRecyclerView(String text) {
        ArrayList<String> filteredList = new ArrayList<>();
        for (String chat : chats) {
            if (chat.toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(chat);
            }
        }
        dataAdapter.updateList(filteredList);
    }


    private ArrayList<String> getChatTitles() {

        ArrayList<String> chatsArrRef = new ArrayList<>();

        DatabaseReference myRefChats = database.getReference("Chats");

        myRefChats.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                String magChat = snapshot.getValue(String.class);

                chatsArrRef.add(magChat);

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


        return chatsArrRef;
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

                DatabaseReference refChats = FirebaseDatabase.getInstance().getReference().child("Chats");
                DatabaseReference refPass = FirebaseDatabase.getInstance().getReference().child("Passwords");

                String chatName = editTextChatName.getText().toString().replace(".", "_");

                refPass.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChild(chatName)) {
                            toast("Такой чат уже существует");
                        } else {

                            if (chatName.trim().isEmpty()){
                                toast("Название чата не может быть пустым");
                            }
                            else{
                                if ((editTextChatPass.getText().toString().length()) <= 5){
                                    toast("Пароль ненадёжный");
                                } else {
                                    DatabaseReference myRefPasswords = database.getReference("Passwords/" + chatName);

                                    myRefChats.push().setValue(chatName);
                                    myRefPasswords.push().setValue(editTextChatPass.getText().toString());

                                    createChat.cancel();

                                    String Username = getIntent().getStringExtra("Username");

                                    intent(chatName, Username);

//                                    toast("Пароль: " + editTextChatPass.getText().toString());

                                }
                            }

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Ошибка при проверке, выводим сообщение
                        toast("Ошибка проверки на индивидуальность");
                    }
                });


//                Toast.makeText(Chats_list.this, "Developers working for this ...", Toast.LENGTH_SHORT).show();
            }
        });

    }







//                                                     MY_CHATS






    public void method_my_chats_from_btn(String username){

    }


    public void intent(String chatName, String Username){

        Intent intent = new Intent(this, Chat1.class);
        intent.putExtra("Chatname", chatName);
        intent.putExtra("Username_to_chat", Username);
        startActivity(intent);
        finish();

    }


    public void toast(String text){
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

}