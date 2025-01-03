package com.example.squery;

import android.view.View;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

public class ChatItemViewHolder extends RecyclerView.ViewHolder {
    TextView nameTextView;

    public ChatItemViewHolder(View itemView) {
        super(itemView);
        nameTextView = itemView.findViewById(R.id.chat_title_item); // Замените на ID вашего TextView в макете
    }
}
