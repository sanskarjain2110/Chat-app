package com.stranger.chat.chat_modules;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.stranger.chat.R;
import com.stranger.chat.adappter.MessagePageAdapter;
import com.stranger.chat.data.MessageData;

public class MessagePage extends AppCompatActivity {

    ImageView backButton, sentButton;
    EditText textMessage;
    RecyclerView messageView;

    Bundle bundle;

    DatabaseReference database;
    MessagePageAdapter messagePageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_page);

        bundle = getIntent().getBundleExtra("data");

        textMessage = findViewById(R.id.getMessage);

        messageView = findViewById(R.id.mainScreen);

        backButton = findViewById(R.id.backButton);
        sentButton = findViewById(R.id.sentButton);

        // set Title
        ((TextView) findViewById(R.id.reciversName)).setText(bundle.getString("username"));

        backButton.setOnClickListener(view -> finish());

        sentButton.setOnClickListener(view -> {
            String text = textMessage.getText().toString();
            if (text.length() != 0) {
                Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
            }
        });

        FirebaseRecyclerOptions<MessageData> options = new FirebaseRecyclerOptions.Builder<MessageData>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("MessageData").child(bundle.getString("messageId")).limitToLast(50), MessageData.class)
                .build();

        FirebaseDatabase.getInstance().getReference().child("users").limitToLast(50).addChildEventListener(new ChildEventListener() {
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

            }
        });

        messagePageAdapter = new MessagePageAdapter(options);
        messageView.setAdapter(messagePageAdapter);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setReverseLayout(true);
        messageView.setLayoutManager(linearLayoutManager);
    }

    protected void onStart() {
        super.onStart();
        messagePageAdapter.startListening();
    }

    protected void onStop() {
        super.onStop();
        messagePageAdapter.stopListening();
    }
}