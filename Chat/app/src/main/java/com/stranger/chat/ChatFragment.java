package com.stranger.chat;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.stranger.chat.adappter.ChatAdapter;
import com.stranger.chat.adappter.StatusAdapter;
import com.stranger.chat.data.Chat_Tile_Data;
import com.stranger.chat.data.Userdata;

import java.util.ArrayList;

public class ChatFragment extends Fragment {
    ArrayList<Chat_Tile_Data> chat_tile_data = new ArrayList<>();

    FloatingActionButton addChat;

    RecyclerView chatRecyclerView, statusRecyclerView;

    ChatAdapter chatAdapter;
    StatusAdapter statusAdapter;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseDatabase mBase = FirebaseDatabase.getInstance();

    FirebaseUser user = mAuth.getCurrentUser();
    DatabaseReference database = mBase.getReference();

    Userdata[] status_data = {
            new Userdata(R.drawable.ic_launcher_background, "Rohan Sharma"),
            new Userdata(R.drawable.ic_launcher_background, "Rohan Sharma"),
            new Userdata(R.drawable.ic_launcher_background, "Rohan Sharma"),
            new Userdata(R.drawable.ic_launcher_background, "Rohan Sharma"),
            new Userdata(R.drawable.ic_launcher_background, "Rohan Sharma"),
            new Userdata(R.drawable.ic_launcher_background, "Rohan Sharma"),
            new Userdata(R.drawable.ic_launcher_background, "Rohan Sharma")
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        addChat = view.findViewById(R.id.addPerson);
//        int chatCount = (int) singleValueDatabase(database.child("users").child("messageCount"));

        addChat.setOnClickListener(view1 -> startActivity(new Intent(getContext(), AddChat.class)));

        database.child("users").child(user.getUid()).child("messageIds").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    chat_tile_data.add(dataSnapshot.getValue(Chat_Tile_Data.class));
                }
                chatAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        statusRecyclerView = view.findViewById(R.id.statusRecyclerView);
        statusAdapter = new StatusAdapter(status_data, getContext());
        statusRecyclerView.setAdapter(statusAdapter);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 1, RecyclerView.HORIZONTAL, false);
        statusRecyclerView.setLayoutManager(gridLayoutManager);

        chatRecyclerView = view.findViewById(R.id.chatRecyclerView);
        chatRecyclerView.setHasFixedSize(true);
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        chatAdapter = new ChatAdapter(chat_tile_data, getContext());
        chatRecyclerView.setAdapter(chatAdapter);

        return view;
    }

    Object singleValueDatabase(@NonNull DatabaseReference reference) {
        final Object[] data = new Object[1];
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                data[0] = snapshot.getValue(Object.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return data[0];
    }
}