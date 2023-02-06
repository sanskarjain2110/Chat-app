package com.stranger.chat.chat_modules;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.stranger.chat.R;
import com.stranger.chat.chat_modules.adapter.MessagePageAdapter;
import com.stranger.chat.chat_modules.data.MessagePage_Tile_Data;
import com.stranger.chat.fuctionality.TimeStamp;

import java.util.HashMap;
import java.util.Map;

public class MessagePage extends AppCompatActivity {

    Toolbar topAppBar;
    FloatingActionButton sentButton;
    EditText textMessage;
    RecyclerView messageView;

    Bundle bundle;
    String messageId;

    FirebaseFirestore database;
    CollectionReference reference;
    Query query;

    MessagePageAdapter messagePageAdapter;
    LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_page);

        bundle = getIntent().getBundleExtra("data");
        messageId = bundle.getString("messageId");

        topAppBar = findViewById(R.id.topAppBar);

        messageView = findViewById(R.id.recyclerview);

        textMessage = findViewById(R.id.getMessage);
        sentButton = findViewById(R.id.sentButton);

        database = FirebaseFirestore.getInstance();
        reference = database.collection("messages").document(messageId).collection("messagesData");
        query = reference.orderBy("timeStamp");

        SharedPreferences sharedPref = getSharedPreferences("localData", Context.MODE_PRIVATE);
        String host_username = sharedPref.getString("host_username", "");

        topAppBar.setTitle(bundle.getString("reciversname"));
        topAppBar.setNavigationOnClickListener(view -> finish());
        topAppBar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.audio_call) {
                Toast.makeText(getApplicationContext(), "Audio", Toast.LENGTH_SHORT).show();
            } else if (item.getItemId() == R.id.video_call) {
                Toast.makeText(getApplicationContext(), "Video", Toast.LENGTH_SHORT).show();
            } else {
                return false;
            }
            return true;
        });

        sentButton.setOnClickListener(view -> {
            String text = textMessage.getText().toString().trim(), dilogId = reference.document().getId();
            if (text.length() != 0) {
                String time = (String) TimeStamp.timeStamp();
                Map<String, Object> pushMessage = new HashMap<>();
                pushMessage.put("sender", host_username);
                pushMessage.put("timeStamp", time);
                pushMessage.put("message", text);
                pushMessage.put("dilogId", dilogId);

                reference.document(dilogId).set(pushMessage).addOnSuccessListener(v -> {
                    Map<String, Object> lastPush = new HashMap<>();
                    lastPush.put("lastSeen", time);
                    FirebaseFirestore.getInstance().collection("messages").document(messageId).update(lastPush);

                    messageView.scrollToPosition(messagePageAdapter.getItemCount() - 1);
                });
                textMessage.setText("");
            }
        });
    }

    protected void onResume() {
        super.onResume();

        FirestoreRecyclerOptions<MessagePage_Tile_Data> options = new FirestoreRecyclerOptions.Builder<MessagePage_Tile_Data>().setQuery(query, MessagePage_Tile_Data.class).build();

        messagePageAdapter = new MessagePageAdapter(options, getSupportFragmentManager(), getApplicationContext(), reference);
        linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        messageView.setLayoutManager(linearLayoutManager);
        messageView.setAdapter(messagePageAdapter);
        messagePageAdapter.startListening();
    }

    protected void onStop() {
        super.onStop();
        messagePageAdapter.stopListening();
    }
}