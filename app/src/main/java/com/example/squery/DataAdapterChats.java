package com.example.squery;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class DataAdapterChats extends RecyclerView.Adapter<ViewHolderChats> {

    ArrayList<String> chats;
    ArrayList<String> filteredChats;
    LayoutInflater inflater;

    public DataAdapterChats(Context context, ArrayList<String> chats){
        this.chats = chats;
        this.filteredChats = new ArrayList<>(chats);
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolderChats onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.chat_item_element, parent, false);
        return new ViewHolderChats(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderChats holder, int position) {
        String mag = filteredChats.get(position);
        holder.chats.setText(mag);
    }

    @Override
    public int getItemCount() {
        return filteredChats.size();
    }

    public void updateList(List<String> newList) {
        this.filteredChats = new ArrayList<>(newList);
        notifyDataSetChanged();
    }

    public int getItemIndex(int position) {
        return position; // В этом случае индекс равен позиции в RecyclerView
    }

}
