package com.stranger.chat.chat_modules;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;

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

    MessagePageAdapter messagePageAdapter;

    FirebaseFirestore database;
    CollectionReference reference;
    Query query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_page);

        bundle = getIntent().getBundleExtra("data");
        messageId = bundle.getString("messageId");

        topAppBar = findViewById(R.id.topAppBar);

        textMessage = findViewById(R.id.getMessage);

        messageView = findViewById(R.id.recyclerview);

        sentButton = findViewById(R.id.sentButton);

        database = FirebaseFirestore.getInstance();
        reference = database.collection("messages").document(messageId).collection("messagesData");
        query = reference.orderBy("timeStamp");

        SharedPreferences sharedPref = getSharedPreferences("localData", Context.MODE_PRIVATE);
        String host_username = sharedPref.getString("host_username", "");

        topAppBar.setTitle(bundle.getString("reciversname"));
        topAppBar.setNavigationOnClickListener(view -> finish());

        sentButton.setOnClickListener(view -> {
            String text = textMessage.getText().toString().trim();
            if (text.length() != 0) {
                String time = (String) TimeStamp.timeStamp();
                Map<String, Object> pushMessage = new HashMap<>();
                pushMessage.put("sender", host_username);
                pushMessage.put("timeStamp", time);
                pushMessage.put("message", text);

                reference.add(pushMessage).addOnSuccessListener(v -> lastMessage(time));
                textMessage.setText("");
            }
        });
    }

    protected void onResume() {
        super.onResume();

        FirestoreRecyclerOptions<MessagePage_Tile_Data> options = new FirestoreRecyclerOptions.Builder<MessagePage_Tile_Data>()
                .setQuery(query, MessagePage_Tile_Data.class).build();

        messagePageAdapter = new MessagePageAdapter(options);
        messageView.setAdapter(messagePageAdapter);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        messageView.setLayoutManager(linearLayoutManager);

        messagePageAdapter.startListening();
    }

    protected void onStop() {
        super.onStop();
        messagePageAdapter.stopListening();
    }

    protected void onPause() {
        super.onPause();
    }

    void lastMessage(String time) {
        Map<String, Object> lastPush = new HashMap<>();
        lastPush.put("lastSeen", time);
        FirebaseFirestore.getInstance().collection("messages").document(messageId).update(lastPush);
    }
}