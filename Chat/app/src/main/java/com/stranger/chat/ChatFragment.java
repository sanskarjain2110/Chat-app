package com.stranger.chat;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.stranger.chat.adappter.ChatAdapter;
import com.stranger.chat.adappter.StatusAdapter;
import com.stranger.chat.data.Chat_Tile_Data;
import com.stranger.chat.data.Userdata;

import java.util.ArrayList;

public class ChatFragment extends Fragment {
    RecyclerView chatRecyclerView, statusRecyclerView;
    ArrayList<Chat_Tile_Data> chat_tile_data = new ArrayList<>();
    ChatAdapter chatAdapter;
    StatusAdapter statusAdapter;

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

        FirebaseDatabase.getInstance().getReference().child("users").addValueEventListener(new ValueEventListener() {
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
}