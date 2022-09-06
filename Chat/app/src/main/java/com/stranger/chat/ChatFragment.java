package com.stranger.chat;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.stranger.chat.adappter.ChatAdapter;
import com.stranger.chat.chat_modules.AddChat;
import com.stranger.chat.data.Chat_Tile_Data;

import java.util.ArrayList;
import java.util.Objects;

public class ChatFragment extends Fragment {
    ArrayList<Chat_Tile_Data> chat_tile_data = new ArrayList<>();

    RecyclerView chatRecyclerView;

    ChatAdapter chatAdapter;

    String user = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        //Floating button
        view.findViewById(R.id.addPerson).setOnClickListener(view1 -> startActivity(new Intent(getContext(), AddChat.class)));

        dataFetching();

        chatRecyclerView = view.findViewById(R.id.chatRecyclerView);
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        chatAdapter = new ChatAdapter(chat_tile_data, getContext());
        chatRecyclerView.setAdapter(chatAdapter);

        return view;
    }

    //MessageKey -->
    void dataFetching() {
        //  usersMessageId --> user --> userKey = MessageKey (pairs)
        FirebaseDatabase.getInstance().getReference().child("usersMessageId").child(user).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Chat_Tile_Data info = new Chat_Tile_Data();

                    String messageId = dataSnapshot.getValue(String.class);
                    String userId = dataSnapshot.getKey();

                    // users --> userID --> "username" = username
                    FirebaseDatabase.getInstance().getReference().child("users").child(userId).child("username").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            info.setUsername(snapshot.getValue(String.class));
                            chatAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    //
                    FirebaseDatabase.getInstance().getReference().child("messageData").child(messageId).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            info.setLastSeen(snapshot.child("lastText").getValue(String.class));
                            info.setLastText(snapshot.child("lastSeen").getValue(String.class));

                            chatAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    chat_tile_data.add(info);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "error", Toast.LENGTH_LONG).show();
            }
        });
    }
}