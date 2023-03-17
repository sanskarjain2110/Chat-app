package com.stranger.chat.chat_modules;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.Query;
import com.stranger.chat.R;
import com.stranger.chat.chat_modules.adapter.ChatPageAdapter;
import com.stranger.chat.chat_modules.bottom_sheet.ChatPage_BottomSheet;
import com.stranger.chat.chat_modules.data.ChatPage_Tile_Data;
import com.stranger.chat.fuctionality.FirebaseDatabaseConnection;
import com.stranger.chat.fuctionality.Helper;

import java.util.HashMap;
import java.util.Map;

public class ChatPage extends AppCompatActivity {

    Toolbar topAppBar;
    RecyclerView chatRecyclerView;
    EditText textMessage;
    ImageView camera, audio, menuImage, sentButton;

    Bundle bundle;
    String messageId;

    CollectionReference reference;
    Query query;

    ChatPageAdapter chatPageAdapter;
    LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_page);

        bundle = getIntent().getBundleExtra("data");
        messageId = bundle.getString("messageId");

        topAppBar = findViewById(R.id.topAppBar);

        chatRecyclerView = findViewById(R.id.recyclerview);

        textMessage = findViewById(R.id.getMessage);
        camera = findViewById(R.id.camera);
        audio = findViewById(R.id.audio);
        menuImage = findViewById(R.id.menuImage);
        sentButton = findViewById(R.id.sentButton);

        reference = FirebaseDatabaseConnection.messageDataCollection(messageId);
        query = reference.orderBy("timeStamp");

        topAppBar.setTitle(bundle.getString("reciverName"));
        topAppBar.setNavigationOnClickListener(view -> finish());
        topAppBar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.audio_call) {
                Toast.makeText(getApplicationContext(), "Audio", Toast.LENGTH_SHORT).show();
            } else if (item.getItemId() == R.id.video_call) {
                Toast.makeText(getApplicationContext(), "Video", Toast.LENGTH_SHORT).show();
            } else return false;
            return true;
        });

        textMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (textMessage.getText().toString().length() != 0) {
                    camera.setVisibility(View.GONE);
                    audio.setVisibility(View.GONE);

                    sentButton.setVisibility(View.VISIBLE);
                } else {
                    camera.setVisibility(View.VISIBLE);
                    audio.setVisibility(View.VISIBLE);

                    sentButton.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        camera.setOnClickListener(view -> {
        });

        audio.setOnClickListener(view -> {
        });

        menuImage.setOnClickListener(view -> {
            ChatPage_BottomSheet chatPage_bottomSheet = new ChatPage_BottomSheet(this);
            chatPage_bottomSheet.show(getSupportFragmentManager(), "menu");
        });

        sentButton.setOnClickListener(view -> {
            String text = textMessage.getText().toString().trim(),
                    dilogId = FirebaseDatabaseConnection.randomId();
            if (text.length() != 0) {
                String time = (String) Helper.timeStamp();
                Map<String, Object> pushMessage = new HashMap<>();
                pushMessage.put("sender", FirebaseDatabaseConnection.currentUser.getUid());
                pushMessage.put("dilogId", dilogId);
                pushMessage.put("timeStamp", time);
                pushMessage.put("message", text);
                pushMessage.put("type", "text");

                reference.document(dilogId).set(pushMessage).addOnSuccessListener(v -> {
                    Map<String, Object> lastPush = new HashMap<>();
                    lastPush.put("lastSeen", time);
                    FirebaseDatabaseConnection.messageDocument(messageId).update(lastPush);

                    chatRecyclerView.scrollToPosition(chatPageAdapter.getItemCount() - 1);
                });
                textMessage.setText("");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == 101 && data != null) {
            Bundle args = new Bundle();
            args.putString("image", String.valueOf(data.getData()));
            args.putString("messageId", messageId);

            Intent intent = new Intent(getApplicationContext(), ImageMessageActivity.class);
            intent.putExtra("data", args);

            startActivity(intent);
        } else {
            Toast.makeText(this, "error", Toast.LENGTH_LONG).show();
        }
    }

    protected void onResume() {
        super.onResume();

        FirestoreRecyclerOptions<ChatPage_Tile_Data> options = new FirestoreRecyclerOptions.Builder<ChatPage_Tile_Data>().setQuery(query, ChatPage_Tile_Data.class).build();

        chatPageAdapter = new ChatPageAdapter(options, getSupportFragmentManager(), this, reference);
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        chatRecyclerView.setLayoutManager(linearLayoutManager);
        chatRecyclerView.setAdapter(chatPageAdapter);
        chatPageAdapter.startListening();
    }

    protected void onStop() {
        super.onStop();
        chatPageAdapter.stopListening();
    }
}