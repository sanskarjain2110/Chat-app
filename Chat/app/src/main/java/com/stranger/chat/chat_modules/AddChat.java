package com.stranger.chat.chat_modules;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.stranger.chat.R;
import com.stranger.chat.chat_modules.adapter.AddChatAdapter;
import com.stranger.chat.chat_modules.data.AddChat_Tile_Data;

import java.util.List;

public class AddChat extends AppCompatActivity {
    AddChatAdapter addChatAdapter;

    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    CollectionReference reference;
    FirebaseFirestore database;
    Query query;

    String currentUserId;

    RecyclerView addChatRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_chat);

        addChatRecyclerView = findViewById(R.id.addChatRecyclerView);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        currentUserId = " ";
        if (currentUser != null) {
            currentUserId = mAuth.getCurrentUser().getUid();
        }

        database = FirebaseFirestore.getInstance();
        reference = database.collection("users");
        query = reference.whereNotEqualTo("userId", currentUserId);

        query.addSnapshotListener((snapshot, error) -> {
            if (error != null) {
                return;
            }

            // Convert query snapshot to a list of chats
            if (snapshot != null) {
                List<AddChat_Tile_Data> chats = snapshot.toObjects(AddChat_Tile_Data.class);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        FirestoreRecyclerOptions<AddChat_Tile_Data> options = new FirestoreRecyclerOptions.Builder<AddChat_Tile_Data>()
                .setQuery(query, AddChat_Tile_Data.class).build();

        addChatRecyclerView.setHasFixedSize(true);
        addChatAdapter = new AddChatAdapter(options, this, getApplicationContext());
        addChatRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        addChatRecyclerView.setAdapter(addChatAdapter);
        addChatAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        addChatAdapter.stopListening();
    }
}