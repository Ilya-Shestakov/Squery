package com.example.squery;


import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import android.view.View;

public class ChatItemAdapter extends RecyclerView.Adapter<ChatItemViewHolder> {

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

    public ChatItem getItemAdapter(int position) {
        if (position >= 0 && position < chatItems.size()) {
            return chatItems.get(position);
        }
        return null; // Или можно выбросить исключение IllegalArgumentException
    }

}
