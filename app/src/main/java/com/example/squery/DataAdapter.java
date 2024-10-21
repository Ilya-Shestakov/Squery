package com.example.squery;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DataAdapter extends RecyclerView.Adapter<ViewHolderMess> {

    ArrayList<String> messages;
    LayoutInflater inflater;

    public DataAdapter(Context context, ArrayList<String> messages){
        this.messages = messages;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolderMess onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.mess_item_element, parent, false);

        return new ViewHolderMess(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderMess holder, int position) {
        String mag = messages.get(position);
        holder.messages.setText(mag);
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }
}
