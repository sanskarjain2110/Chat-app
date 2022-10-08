package com.stranger.chat.chat_modules;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
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

    FirebaseFirestore database;
    CollectionReference reference;
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

        database = FirebaseFirestore.getInstance();
        reference = database.collection("messages").document(messageId).collection("messagesData");
        query = reference.limitToLast(50).orderBy("timeStamp");

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
                reference.add(pushMessage);

                lastText = text;
                lastTimeStamp = currentDateandTime;
                textMessage.setText("");
            }
        });

        query.addSnapshotListener((snapshot, error) -> {

        });

        FirestoreRecyclerOptions<MessageData> options = new FirestoreRecyclerOptions.Builder<MessageData>()
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
        if (lastText != null) {
            Map<String, Object> lastPush = new HashMap<>();
            lastPush.put("lastText", lastText);
            lastPush.put("lastSeen", lastTimeStamp);
            FirebaseFirestore.getInstance().collection("messages").document(messageId).update(lastPush);
        }
    }
}