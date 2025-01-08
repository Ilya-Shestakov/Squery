package com.example.squery;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import android.util.Log;
import android.content.Context;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;


public class FirebaseChatManager implements LifecycleObserver {
    private static final String TAG = "FirebaseChatManager";
    private final DatabaseReference chatRef;
    ChildEventListener childEventListener;
    private ValueEventListener valueEventListener;
    private boolean isChatActivityActive = false;
    public  FirebaseChatManager(Context context, String chatname){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        chatRef = database.getReference("ChatsWithMess/" + chatname);
    }


    public void startListeningForNewMessages(Context c, String chatname) {
        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Check if ChatActivity is not active
                    if (!isChatActivityActive) {
                        NotificationHelper notificationHelper = new NotificationHelper(c);
                        notificationHelper.sendNotification("Squery", "Новое сообщение в " + chatname);
                    }
                } else {
                    Log.d(TAG, "Данные удалены");
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.e(TAG, "Слушатель отменен", error.toException());
            }
        };
        chatRef.addValueEventListener(valueEventListener);
    }

    public void setChatActivityActive(boolean isActive) {
        isChatActivityActive = isActive;
    }
    public void stopListeningForNewMessages() {
        if (valueEventListener != null) {
            chatRef.removeEventListener(valueEventListener);
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onStart(){
        setChatActivityActive(false);
    }
    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onStop(){
        setChatActivityActive(true);
    }
}