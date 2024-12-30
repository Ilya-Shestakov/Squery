package com.example.squery;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.NotificationCompat;

import java.lang.reflect.Modifier;

public class MainActivity extends AppCompatActivity {

    MessageListenerService tracker = new MessageListenerService();

    private static final int REQUEST_CODE_POST_NOTIFICATIONS = 1;

    private AuthHelper authHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        authHelper = new AuthHelper(this);

        if (authHelper.isLoggedIn()) {

            Intent intent = new Intent(MainActivity.this, Chats_list.class);
            intent.putExtra("Username", authHelper.getUsername());
            startActivity(intent);

            finish(); // Закрываем MainActivity после успешного автологина
        } else {
            // Переход к экрану входа
            startActivity(new Intent(MainActivity.this, sing_up.class));
            finish();
        }

        //Stoper
        //tracker.stopTracking();

//        tracker.startTracking("чат 1", new MessageListenerService.ChatUpdateListener() {
//            @Override
//            public void onUpdate(String chatId) {
//                sendMessFirstChat();
//            }
//        });



        // Проверка версии Android
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Проверка, предоставлено ли разрешение
            if (checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                // Запрос разрешения у пользователя
                requestPermissions(new String[]{Manifest.permission.POST_NOTIFICATIONS}, REQUEST_CODE_POST_NOTIFICATIONS);
            } else {
                // Разрешение уже предоставлено, можно отправлять уведомления
            }
        } else {
            // Для Android ниже Android 13 разрешение не требуется
        }


    }

    private void sendMessFirstChat() {
        NotificationHelper notificationHelper = new NotificationHelper(this);
        notificationHelper.sendNotification("New message", "Новое сообщение");
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE_POST_NOTIFICATIONS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Разрешение предоставлено, можно отправлять уведомления
            } else {
                // Разрешение отклонено, не можем отправлять уведомления
            }
        }
    }


    public void sing_in(View view) {

//        Toast.makeText(this,"xex", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this, sing_in.class);
        startActivity(intent);
        finish();
    }

    public void sing_up(View view) {

//        Toast.makeText(this,"kek", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this, sing_up.class);
        startActivity(intent);
        finish();

    }

}