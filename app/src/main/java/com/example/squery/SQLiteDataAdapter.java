package com.example.squery;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class SQLiteDataAdapter extends RecyclerView.Adapter<SQLiteDataAdapter.ViewHolder> {

    public static List<ChatItem> chatItems;
    private LayoutInflater inflater;

    public SQLiteDataAdapter(Context context, List<ChatItem> chatItems) {
        this.chatItems = chatItems;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.chat_item_element, parent, false);
        return new ViewHolder(view);
    }

    public static ChatItem getItemPozInMyChats(int position) {
        if (position >= 0 && position < chatItems.size()) {
            return chatItems.get(position);
        }
        return null; // Или можно выбросить исключение IllegalArgumentException
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ChatItem item = chatItems.get(position);
        holder.chatTitle.setText(item.getChatName());
    }

    @Override
    public int getItemCount() {
        return chatItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView chatTitle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            chatTitle = itemView.findViewById(R.id.chat_title_item);
        }
    }

    public static int getItemIndexForMyChats(int position) {
        return position; // В этом случае индекс равен позиции в RecyclerView
    }

}
