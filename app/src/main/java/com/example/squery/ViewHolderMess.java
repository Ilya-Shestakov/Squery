package com.example.squery;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

public class ViewHolderMess extends RecyclerView.ViewHolder {
    TextView messages;
    TextView time;
    ViewHolderMess(View view){
        super(view);
        messages = view.findViewById(R.id.message_of_send_mess);
        if(messages == null){
        }
        time = view.findViewById(R.id.time_of_send_mess);
        if(time == null){

        }
    }
}
