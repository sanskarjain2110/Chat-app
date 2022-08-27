package com.stranger.chat;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.stranger.chat.adappter.MessagePageAdapter;

public class MessagePage extends AppCompatActivity {

    RecyclerView messageView;
    DatabaseReference database;
    MessagePageAdapter messagePageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_page);

        messageView = findViewById(R.id.mainScreen);
        messagePageAdapter = new MessagePageAdapter();
        messageView.setAdapter(messagePageAdapter);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setReverseLayout(true);
        messageView.setLayoutManager(linearLayoutManager);

    }
}