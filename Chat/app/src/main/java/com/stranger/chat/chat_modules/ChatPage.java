package com.stranger.chat.chat_modules;

import static com.stranger.chat.fuctionality.Keys.BUNDLE_DATA;
import static com.stranger.chat.fuctionality.Keys.firebase.MESSAGE_ID;
import static com.stranger.chat.fuctionality.Keys.firebase.USERNAME;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.Query;
import com.google.firebase.messaging.FirebaseMessaging;
import com.stranger.chat.R;
import com.stranger.chat.chat_modules.adapter.ChatPageAdapter;
import com.stranger.chat.chat_modules.bottom_sheet.ChatPage_BottomSheet;
import com.stranger.chat.chat_modules.data.ChatPage_Tile_Data;
import com.stranger.chat.fuctionality.FirebaseDatabaseConnection;
import com.stranger.chat.fuctionality.Text;

import java.util.HashMap;
import java.util.Map;

public class ChatPage extends AppCompatActivity {

    Toolbar topAppBar;
    RecyclerView chatRecyclerView;
    EditText textMessage;
    Button camera, audio, menuImage, sentButton;

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

        bundle = getIntent().getBundleExtra(BUNDLE_DATA);
        messageId = bundle.getString(MESSAGE_ID);

        topAppBar = findViewById(R.id.topAppBar);

        chatRecyclerView = findViewById(R.id.recyclerview);

        textMessage = findViewById(R.id.getMessage);
        camera = findViewById(R.id.camera);
        audio = findViewById(R.id.audio);
        menuImage = findViewById(R.id.menuImage);
        sentButton = findViewById(R.id.sentButton);

        reference = FirebaseDatabaseConnection.messageDataCollection(messageId);
        query = reference.orderBy("timeStamp").limitToLast(50);

        topAppBar.setTitle(bundle.getString(USERNAME));
        topAppBar.setNavigationOnClickListener(view -> finish());
        topAppBar.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), ConversationSetting.class);
            intent.putExtra(BUNDLE_DATA, bundle);
            startActivity(intent);
        });
        topAppBar.setOnMenuItemClickListener(item -> {
            String text = null;
            if (item.getItemId() == R.id.video_call) text = "Video";
            else if (item.getItemId() == R.id.audio_call) text = "Audio";
            else if (item.getItemId() == R.id.disappearMessages) text = "Disappearing Message";
            else if (item.getItemId() == R.id.allMedia) text = "All media";
            else if (item.getItemId() == R.id.conversationSetting) {
                Intent intent = new Intent(getApplicationContext(), ConversationSetting.class);
                intent.putExtra(BUNDLE_DATA, bundle);
                startActivity(intent);
            } else if (item.getItemId() == R.id.search) text = "Search";
            else if (item.getItemId() == R.id.add_to_home_screen) text = "add to hame screen";
            else if (item.getItemId() == R.id.muteNotification) text = "Mute notification";
            else return false;

            if (text != null)
                Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
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
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, 100);
        });

        audio.setOnClickListener(view -> Toast.makeText(this,
                "not implemented", Toast.LENGTH_SHORT).show());

        menuImage.setOnClickListener(view -> {
            ChatPage_BottomSheet chatPage_bottomSheet = new ChatPage_BottomSheet();
            chatPage_bottomSheet.show(getSupportFragmentManager(), "menu");
        });

        sentButton.setOnClickListener(view -> {
            String text = textMessage.getText().toString().trim(),
                    dilogId = FirebaseDatabaseConnection.randomId();
            if (text.length() != 0) {
                String time = new Text(this).timeStamp();
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

                FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) return;

                    // Get new FCM registration token
                    String token = task.getResult();

                    // Log and toast
                    String msg = text;
                    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
                });
                textMessage.setText("");
            }

        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null || requestCode == 0) return;

        if (resultCode == Activity.RESULT_OK && requestCode == 101) {
            Bundle args = new Bundle();
            args.putString("image", String.valueOf(data.getData()));
            args.putString(MESSAGE_ID, messageId);

            Intent intent = new Intent(getApplicationContext(), ImageMessageActivity.class);
            intent.putExtra(BUNDLE_DATA, args);

            startActivity(intent);
        } else {
            Toast.makeText(this, "error", Toast.LENGTH_LONG).show();
        }
    }

    protected void onResume() {
        super.onResume();

        FirestoreRecyclerOptions<ChatPage_Tile_Data> options = new FirestoreRecyclerOptions.Builder<ChatPage_Tile_Data>().setQuery(query, ChatPage_Tile_Data.class).build();

        chatPageAdapter = new ChatPageAdapter(options, this, reference);
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