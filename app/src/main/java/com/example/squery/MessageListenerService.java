package com.example.squery;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


//                                                          ОТСЛЕЖИВАНИЕ НОВЫХ СООБЩЕНИЙ


public class MessageListenerService {

    private DatabaseReference chatsWithMessRef;
    private ValueEventListener listener;

    public MessageListenerService() {
        // Настройка ссылки на базу данных
        chatsWithMessRef = FirebaseDatabase.getInstance().getReference("ChatsWithMess");
    }

    public void startTracking(String chatId, ChatUpdateListener updateListener) {
        // Создание слушателя событий
        listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Проверка, изменилось ли значение в чате
                if (dataSnapshot.hasChild(chatId)) {
                    // Если значение изменилось, вызываем обработчик обновления
                    updateListener.onUpdate(chatId);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Обработка ошибок
                // ...
            }
        };

        // Настройка слушателя для всего каталога "ChatsWithMess"
        chatsWithMessRef.addValueEventListener(listener);
    }

    public void stopTracking() {
        // Удаление слушателя событий
        if (listener != null) {
            chatsWithMessRef.removeEventListener(listener);
            listener = null;
        }
    }

    // Интерфейс для обработки обновлений
    public interface ChatUpdateListener {
        void onUpdate(String chatId);
    }

}