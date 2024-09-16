package com.example.squery;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void sing_in(View view) {

        Toast.makeText(this,"xex", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this, sing_in.class);
        startActivity(intent);
        finish();
    }

    public void sing_up(View view) {
        Toast.makeText(this,"kek", Toast.LENGTH_SHORT).show();
    }

}