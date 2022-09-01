package com.stranger.chat;

import static android.widget.Toast.LENGTH_LONG;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.stranger.chat.adappter.AddChatAdapter;
import com.stranger.chat.data.AddChat_Tile_Data;

import java.util.ArrayList;

public class AddChat extends AppCompatActivity {
    ArrayList<AddChat_Tile_Data> data = new ArrayList<>();
    RecyclerView addChatRecyclerView;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_chat);

        addChatRecyclerView = findViewById(R.id.addChatRecyclerView);
        addChatRecyclerView.setHasFixedSize(true);
        AddChatAdapter addChatAdapter = new AddChatAdapter(data, getApplicationContext());
        addChatRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        addChatRecyclerView.setAdapter(addChatAdapter);

        myRef.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    AddChat_Tile_Data info = new AddChat_Tile_Data();

                    info.setPhoneNumber(dataSnapshot.child("phoneNumber").getValue(String.class));
                    info.setUsername(dataSnapshot.child("username").getValue(String.class));
                    info.setUserId(dataSnapshot.getKey());

                    data.add(info);
                }
                addChatAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "problem", LENGTH_LONG).show();
            }
        });


    }
}