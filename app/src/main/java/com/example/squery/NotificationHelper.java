package com.example.squery;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;




//                                                          ОТПРАВКА УВЕДОМЛЕНИЙ


public class NotificationHelper{

    private static final String CHANNEL_ID = "default_channel";
    private static final String CHANNEL_NAME = "Default Channel";
    private static final int NOTIFICATION_ID = 1;

    private Context context;

    public NotificationHelper(Context context) {
        this.context = context;
    }

    public void sendNotification(String title, String message) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Create the NotificationChannel, if necessary.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        // Create an Intent for the notification.
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        // Build the notification.
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.squeryv2) // Замените на свой иконку уведомления
                .setContentTitle(title)
                .setContentText(message)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        // Send the notification.
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    public static class ChatItemAdapter extends RecyclerView.Adapter<ChatItemViewHolder> {

        private List<ChatItem> chatItems;

        public ChatItemAdapter(List<ChatItem> chatItems) {
            this.chatItems = chatItems;
        }

        @NonNull
        @Override
        public ChatItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_element, parent, false);
            return new ChatItemViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ChatItemViewHolder holder, int position) {
            ChatItem chatItem = chatItems.get(position);
            holder.nameTextView.setText(chatItem.getChatName());
        }

        @Override
        public int getItemCount() {
            return chatItems.size();
        }

        public void setChatItems(List<ChatItem> chatItems) {
            this.chatItems = chatItems;
            notifyDataSetChanged();
        }
    }
}