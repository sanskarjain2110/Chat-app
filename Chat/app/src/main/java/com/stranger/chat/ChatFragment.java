package com.stranger.chat;

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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.stranger.chat.adappter.ChatAdapter;
import com.stranger.chat.chat_modules.AddChat;
import com.stranger.chat.data.Chat_Tile_Data;

public class ChatFragment extends Fragment {

    RecyclerView chatRecyclerView;

    FirebaseFirestore database;
    CollectionReference reference;
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

        database = FirebaseFirestore.getInstance();
        reference = database.collection("messages");
        query = reference.whereArrayContains("filter", user);

        query.addSnapshotListener((snapshot, error) -> {
//            if (error != null) {
            // Handle error
            //...
//                return;
//            }

            // Convert query snapshot to a list of chats
//            List<Chat_Tile_Data> list = snapshot.toObjects(Chat_Tile_Data.class);

            // Update UI

        });

        return view;
    }

    public void onResume() {
        super.onResume();
        FirestoreRecyclerOptions<Chat_Tile_Data> options = new FirestoreRecyclerOptions.Builder<Chat_Tile_Data>()
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