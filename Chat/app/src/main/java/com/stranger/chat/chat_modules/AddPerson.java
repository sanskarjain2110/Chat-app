package com.stranger.chat.chat_modules;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;
import com.stranger.chat.R;
import com.stranger.chat.chat_modules.adapter.AddPersonAdapter;
import com.stranger.chat.chat_modules.data.AddChat_Tile_Data;
import com.stranger.chat.fuctionality.FirebaseConnections;

public class AddPerson extends AppCompatActivity {
    AddPersonAdapter addPersonAdapter;

    Query userCollectionQuery;

    String currentUserId = FirebaseConnections.currentUser.getUid();
    RecyclerView addChatRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_person);

        addChatRecyclerView = findViewById(R.id.addChatRecyclerView);

        userCollectionQuery = FirebaseConnections.userCollection.whereNotEqualTo("userId", currentUserId);
    }

    @Override
    protected void onResume() {
        super.onResume();

        FirestoreRecyclerOptions<AddChat_Tile_Data> options = new FirestoreRecyclerOptions.Builder<AddChat_Tile_Data>()
                .setQuery(userCollectionQuery, AddChat_Tile_Data.class).build();

        addChatRecyclerView.setHasFixedSize(true);
        addPersonAdapter = new AddPersonAdapter(options, this, getApplicationContext());
        addChatRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        addChatRecyclerView.setAdapter(addPersonAdapter);
        addPersonAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        addPersonAdapter.stopListening();
    }
}