package com.stranger.chat.chat_modules;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.stranger.chat.R;
import com.stranger.chat.adappter.AddChatAdapter;
import com.stranger.chat.chat_modules.data.AddChat_Tile_Data;

import java.util.List;

public class AddChat extends AppCompatActivity {
    AddChatAdapter addChatAdapter;

    FirebaseFirestore database;
    Query query;

    SharedPreferences sharedPref;
    String host_username;

    RecyclerView addChatRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_chat);

        sharedPref = getSharedPreferences("localData", Context.MODE_PRIVATE);
        host_username = sharedPref.getString("host_username", " ");

        addChatRecyclerView = findViewById(R.id.addChatRecyclerView);
    }

    @Override
    protected void onResume() {
        super.onResume();

        database = FirebaseFirestore.getInstance();
        query = database.collection("users");

        query.addSnapshotListener((snapshot, error) -> {
            if (error != null) {
                // Handle error
                //...
                return;
            }

            // Convert query snapshot to a list of chats
            List<AddChat_Tile_Data> chats = snapshot.toObjects(AddChat_Tile_Data.class);

            // Update UI
            // ...
        });

        FirestoreRecyclerOptions<AddChat_Tile_Data> options = new FirestoreRecyclerOptions.Builder<AddChat_Tile_Data>()
                .setQuery(query, AddChat_Tile_Data.class).build();

        addChatRecyclerView.setHasFixedSize(true);
        addChatAdapter = new AddChatAdapter(options, this, host_username);
        addChatRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        addChatRecyclerView.setAdapter(addChatAdapter);
        addChatAdapter.startListening();
    }

    protected void onStop() {
        super.onStop();
        addChatAdapter.stopListening();
    }
}