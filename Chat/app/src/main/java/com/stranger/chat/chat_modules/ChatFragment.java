package com.stranger.chat.chat_modules;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.stranger.chat.R;
import com.stranger.chat.chat_modules.adapter.ChatFragmentAdapter;
import com.stranger.chat.chat_modules.data.ChatFragment_Tile_Data;

import java.util.List;

public class ChatFragment extends Fragment {

    RecyclerView chatRecyclerView;

    FirebaseAuth mAuth;
    FirebaseUser user;
    FirebaseFirestore database;
    CollectionReference reference;
    Query query;

    ChatFragmentAdapter chatAdapter;

    String currentUser;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        //Floating button
        view.findViewById(R.id.addPerson).setOnClickListener(view1 -> startActivity(new Intent(getContext(), AddChat.class)));

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        currentUser = " ";
        if (user != null) {
            currentUser = user.getUid();
        }

        chatRecyclerView = view.findViewById(R.id.chatRecyclerView);

        database = FirebaseFirestore.getInstance();
        reference = database.collection("messages");
        query = reference.whereArrayContains("usersId", currentUser).orderBy("lastSeen");

        query.addSnapshotListener((snapshot, error) -> {
            if (error != null) {
                return;
            }

            // Convert query snapshot to a list of chats
            if (snapshot != null) {
                List<ChatFragment_Tile_Data> list = snapshot.toObjects(ChatFragment_Tile_Data.class);
            }
        });

        return view;
    }

    public void onResume() {
        super.onResume();
        FirestoreRecyclerOptions<ChatFragment_Tile_Data> options = new FirestoreRecyclerOptions.Builder<ChatFragment_Tile_Data>()
                .setQuery(query, ChatFragment_Tile_Data.class).build();

        chatAdapter = new ChatFragmentAdapter(options, getContext(), currentUser);
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        chatRecyclerView.setAdapter(chatAdapter);

        chatAdapter.startListening();
    }

    public void onStop() {
        super.onStop();
        chatAdapter.stopListening();
    }
}