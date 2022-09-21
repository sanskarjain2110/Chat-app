package com.stranger.chat;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.stranger.chat.adappter.ChatAdapter;
import com.stranger.chat.chat_modules.AddChat;
import com.stranger.chat.data.Chat_Tile_Data;

public class ChatFragment extends Fragment {

    RecyclerView chatRecyclerView;

    FirebaseDatabase database;
    DatabaseReference reference;
    Query query;

    ChatAdapter chatAdapter;

    String user;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        //Floating button
        view.findViewById(R.id.addPerson).setOnClickListener(view1 -> startActivity(new Intent(getContext(), AddChat.class)));

        user = FirebaseAuth.getInstance().getCurrentUser().getUid();

        chatRecyclerView = view.findViewById(R.id.chatRecyclerView);

        database = FirebaseDatabase.getInstance();
        reference = database.getReference().child("messagesId");
        query = reference.limitToLast(50).orderByChild(user);

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
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        return view;
    }

    public void onResume() {
        super.onResume();

        FirebaseRecyclerOptions<Chat_Tile_Data> options = new FirebaseRecyclerOptions.Builder<Chat_Tile_Data>()
                .setQuery(query, Chat_Tile_Data.class).build();

        chatAdapter = new ChatAdapter(options, getContext());
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        chatRecyclerView.setAdapter(chatAdapter);
        chatAdapter.startListening();
    }


    public void onStop() {
        super.onStop();
        chatAdapter.stopListening();
    }
}