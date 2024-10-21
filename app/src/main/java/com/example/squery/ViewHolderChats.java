package com.example.squery;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class ViewHolderChats extends RecyclerView.ViewHolder {

    TextView chats;

    public ViewHolderChats(View itemView) {
        super(itemView);
        chats = itemView.findViewById(R.id.chat_title_item);
    }
}
