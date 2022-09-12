package com.stranger.chat.chat_modules;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.google.firebase.database.Query;
import com.stranger.chat.R;
import com.stranger.chat.adappter.MessagePageAdapter;
import com.stranger.chat.data.MessageData;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MessagePage extends AppCompatActivity {

    ImageView backButton, sentButton;
    EditText textMessage;
    TextView reciverName;
    RecyclerView messageView;

    String lastText, lastTimeStamp;

    Bundle bundle;
    String messageId;

    MessagePageAdapter messagePageAdapter;

    FirebaseDatabase database;
    DatabaseReference reference;
    Query query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_page);

        bundle = getIntent().getBundleExtra("data");
        messageId = bundle.getString("messageId");

        textMessage = findViewById(R.id.getMessage);
        reciverName = findViewById(R.id.reciversName);

        messageView = findViewById(R.id.mainScreen);

        backButton = findViewById(R.id.backButton);
        sentButton = findViewById(R.id.sentButton);

        database = FirebaseDatabase.getInstance();
        reference = database.getReference().child("messagesData").child(messageId);
        query = reference.limitToLast(50).orderByChild("timeStamp");

        SharedPreferences sharedPref = getSharedPreferences("localData", Context.MODE_PRIVATE);
        String host_username = sharedPref.getString("host_username", "");

        // set Title
        reciverName.setText(bundle.getString("reciverUsername"));

        backButton.setOnClickListener(view -> finish());

        sentButton.setOnClickListener(view -> {
            String text = textMessage.getText().toString();
            if (text.length() != 0) {

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
                String currentDateandTime = sdf.format(new Date());

                Map<String, Object> pushMessage = new HashMap<>();
                pushMessage.put("sender", host_username);
                pushMessage.put("timeStamp", currentDateandTime);
                pushMessage.put("message", text);
                reference.push().setValue(pushMessage);

                lastText = text;
                lastTimeStamp = currentDateandTime;
                textMessage.setText("");
            }
        });

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

            }
        });

        FirebaseRecyclerOptions<MessageData> options = new FirebaseRecyclerOptions.Builder<MessageData>()
                .setQuery(query, MessageData.class).build();

        messagePageAdapter = new MessagePageAdapter(options);
        messageView.setAdapter(messagePageAdapter);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        messageView.setLayoutManager(linearLayoutManager);
    }

    protected void onStart() {
        super.onStart();
        messagePageAdapter.startListening();
    }

    protected void onStop() {
        super.onStop();
        messagePageAdapter.stopListening();

        lastUpdate();
    }

    protected void onPause() {
        super.onPause();

        lastUpdate();
    }

    void lastUpdate() {
        Map<String, Object> lastPush = new HashMap<>();
        lastPush.put("lastText", lastText);
        lastPush.put("lastSeen", lastTimeStamp);
        FirebaseDatabase.getInstance().getReference().child("messagesId").child(messageId).updateChildren(lastPush);
    }
}