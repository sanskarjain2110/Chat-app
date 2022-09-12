package com.stranger.chat.chat_modules;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.stranger.chat.R;
import com.stranger.chat.adappter.AddChatAdapter;
import com.stranger.chat.data.AddChat_Tile_Data;

import java.util.ArrayList;

public class AddChat extends AppCompatActivity {
    ArrayList<AddChat_Tile_Data> data = new ArrayList<>();

    AddChatAdapter addChatAdapter;


    FirebaseDatabase database;
    DatabaseReference reference;
    Query query;

    RecyclerView addChatRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_chat);

        SharedPreferences sharedPref = getSharedPreferences("localData", Context.MODE_PRIVATE);
        String host_username = sharedPref.getString("host_username", "");

        database = FirebaseDatabase.getInstance();
        reference = database.getReference().child("users");
        query = reference.limitToLast(60);

        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        FirebaseRecyclerOptions<AddChat_Tile_Data> options = new FirebaseRecyclerOptions.Builder<AddChat_Tile_Data>()
                .setQuery(query, AddChat_Tile_Data.class).build();

        addChatRecyclerView = findViewById(R.id.addChatRecyclerView);
        addChatRecyclerView.setHasFixedSize(true);
        addChatAdapter = new AddChatAdapter(options, this,host_username);
        addChatRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        addChatRecyclerView.setAdapter(addChatAdapter);
    }

    protected void onStart() {
        super.onStart();
        addChatAdapter.startListening();
    }

    protected void onStop() {
        super.onStop();
        addChatAdapter.stopListening();
    }
}