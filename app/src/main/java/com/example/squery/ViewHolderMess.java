package com.example.squery;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class ViewHolderMess extends RecyclerView.ViewHolder {

    TextView messages;

    public ViewHolderMess(View itemView) {
        super(itemView);
        messages = itemView.findViewById(R.id.Mess1);
    }
}
