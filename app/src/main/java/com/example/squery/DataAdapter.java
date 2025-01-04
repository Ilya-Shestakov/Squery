package com.example.squery;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolderMess> {

    private ArrayList<MessageData> messages;
    private LayoutInflater inflater;

    public DataAdapter(Context context, ArrayList<MessageData> messages) {
        this.messages = messages;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolderMess onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.mess_time_item_element, parent, false);
        return new ViewHolderMess(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderMess holder, int position) {
        if (messages == null || position < 0 || position >= messages.size()) {
            Log.e("FirebaseData", "Invalid data in onBindViewHolder. messages is null or position is out of range: " + position);
            return;
        }
        MessageData data = messages.get(position);
        if(data == null){
            Log.e("FirebaseData", "Message data is null in onBindViewHolder at position: " + position);
            return;
        }

        holder.messages.setText(data.getMessage());
        holder.time.setText(data.getTime());
    }

    @Override
    public int getItemCount() {
        return messages == null ? 0 : messages.size();
    }

    public void setMessages(ArrayList<MessageData> messages) {
        this.messages = messages;
        notifyDataSetChanged();
    }

    public static class ViewHolderMess extends RecyclerView.ViewHolder {
        TextView messages;
        TextView time;

        ViewHolderMess(View view) {
            super(view);
            messages = view.findViewById(R.id.message_of_send_mess);
            if(messages == null){
                Log.e("FirebaseData", "message TextView not found in ViewHolder");
            }
            time = view.findViewById(R.id.time_of_send_mess);
            if(time == null){
                Log.e("FirebaseData", "time TextView not found in ViewHolder");
            }
        }
    }
}