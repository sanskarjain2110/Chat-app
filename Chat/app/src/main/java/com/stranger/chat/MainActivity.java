package com.stranger.chat;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.stranger.chat.call_module.CallAdapter;
import com.stranger.chat.call_module.data.Userdata;
import com.stranger.chat.chat_modules.AddChat;
import com.stranger.chat.chat_modules.adapter.ChatFragmentAdapter;
import com.stranger.chat.chat_modules.data.ChatFragment_Tile_Data;
import com.stranger.chat.notes_modules.ContentActivity;
import com.stranger.chat.notes_modules.adapter.Notes_Adapter;
import com.stranger.chat.notes_modules.data.Topics_Tile_Data;
import com.stranger.chat.settings.Settings;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    Userdata[] data = {
            new Userdata(R.drawable.ic_launcher_background, R.drawable.ic_launcher_background, R.drawable.ic_launcher_background, "Rohan", "20/07/2022, 07:30 a.m."),
            new Userdata(R.drawable.ic_launcher_background, R.drawable.ic_launcher_background, R.drawable.ic_launcher_background, "Rohan", "20/07/2022, 07:30 a.m."),
            new Userdata(R.drawable.ic_launcher_background, R.drawable.ic_launcher_background, R.drawable.ic_launcher_background, "Rohan", "20/07/2022, 07:30 a.m."),
            new Userdata(R.drawable.ic_launcher_background, R.drawable.ic_launcher_background, R.drawable.ic_launcher_background, "Rohan", "20/07/2022, 07:30 a.m."),
            new Userdata(R.drawable.ic_launcher_background, R.drawable.ic_launcher_background, R.drawable.ic_launcher_background, "Rohan", "20/07/2022, 07:30 a.m."),
            new Userdata(R.drawable.ic_launcher_background, R.drawable.ic_launcher_background, R.drawable.ic_launcher_background, "Rohan", "20/07/2022, 07:30 a.m."),
            new Userdata(R.drawable.ic_launcher_background, R.drawable.ic_launcher_background, R.drawable.ic_launcher_background, "Rohan", "20/07/2022, 07:30 a.m."),
            new Userdata(R.drawable.ic_launcher_background, R.drawable.ic_launcher_background, R.drawable.ic_launcher_background, "Rohan", "20/07/2022, 07:30 a.m."),
            new Userdata(R.drawable.ic_launcher_background, R.drawable.ic_launcher_background, R.drawable.ic_launcher_background, "Rohan", "20/07/2022, 07:30 a.m."),
            new Userdata(R.drawable.ic_launcher_background, R.drawable.ic_launcher_background, R.drawable.ic_launcher_background, "Rohan", "20/07/2022, 07:30 a.m."),
            new Userdata(R.drawable.ic_launcher_background, R.drawable.ic_launcher_background, R.drawable.ic_launcher_background, "Rohan", "20/07/2022, 07:30 a.m."),
            new Userdata(R.drawable.ic_launcher_background, R.drawable.ic_launcher_background, R.drawable.ic_launcher_background, "Rohan", "20/07/2022, 07:30 a.m."),
            new Userdata(R.drawable.ic_launcher_background, R.drawable.ic_launcher_background, R.drawable.ic_launcher_background, "Rohan", "20/07/2022, 07:30 a.m."),
            new Userdata(R.drawable.ic_launcher_background, R.drawable.ic_launcher_background, R.drawable.ic_launcher_background, "Rohan", "20/07/2022, 07:30 a.m."),
            new Userdata(R.drawable.ic_launcher_background, R.drawable.ic_launcher_background, R.drawable.ic_launcher_background, "Rohan", "20/07/2022, 07:30 a.m."),
            new Userdata(R.drawable.ic_launcher_background, R.drawable.ic_launcher_background, R.drawable.ic_launcher_background, "Rohan", "20/07/2022, 07:30 a.m."),
    };

    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    String currentUserId;

    CollectionReference chatReference, nosteReference;
    Query chatQuery, noteQuery;

    ChatFragmentAdapter chatFragmentAdapter;
    CallAdapter callAdapter;
    Notes_Adapter notesAdapter;

    Toolbar topAppBar;

    RecyclerView recyclerview;

    FloatingActionButton addButton;
    BottomAppBar bottomAppBar;
    int selectedMenuItemInBotomAppBar;

    SearchView searchBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        currentUserId = currentUser.getUid();

        nosteReference = FirebaseFirestore.getInstance().collection("users").document(currentUserId).collection("notes");
        noteQuery = nosteReference.orderBy("lastUpdated", Query.Direction.DESCENDING);
        chatReference = FirebaseFirestore.getInstance().collection("messages");
        chatQuery = chatReference.whereArrayContains("usersId", currentUserId).orderBy("lastSeen");

        topAppBar = findViewById(R.id.topAppBar);

        recyclerview = findViewById(R.id.recyclerview);

        addButton = findViewById(R.id.addButton);
        bottomAppBar = findViewById(R.id.bottom_navigation);
        selectedMenuItemInBotomAppBar = R.id.chatNavigationButton;

        searchBar = findViewById(R.id.searchBar);

        topAppBar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.settingsButton) {
                startActivity(new Intent(getApplicationContext(), Settings.class));
            } else {
                return false;
            }
            return true;
        });

        addButton.setOnClickListener(view -> {
            if (selectedMenuItemInBotomAppBar == R.id.chatNavigationButton) {
                startActivity(new Intent(getApplicationContext(), AddChat.class));
            } else if (selectedMenuItemInBotomAppBar == R.id.callNavigationButton) {
                Toast.makeText(this, "hello", Toast.LENGTH_SHORT).show();
            } else if (selectedMenuItemInBotomAppBar == R.id.notesNavigationButton) {
                String key = nosteReference.document().getId();
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.US);
                String currentDateandTime = sdf.format(new Date());

                Map<String, Object> data = new HashMap<>();
                data.put("topicId", key);
                data.put("topic", key);
                data.put("lastUpdated", currentDateandTime);

                // create new iten and open that for editing
                nosteReference.document(key).set(data).addOnSuccessListener(unused -> {
                    Intent intent = new Intent(getApplicationContext(), ContentActivity.class);
                    intent.putExtra("topicId", key);
                    startActivity(intent);
                }).addOnFailureListener(e -> Toast.makeText(getApplicationContext(), e + " ", Toast.LENGTH_SHORT).show());
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        chatView();
        callView();
        notesView();

        recyclerview.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerview.setAdapter(chatFragmentAdapter);
        topAppBar.setTitle(R.string.chats);

        bottomAppBar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.chatNavigationButton) {
                recyclerview.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                recyclerview.setAdapter(chatFragmentAdapter);

                topAppBar.setTitle(R.string.chats);
            } else if (item.getItemId() == R.id.callNavigationButton) {
                recyclerview.setAdapter(callAdapter);
                recyclerview.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

                topAppBar.setTitle(R.string.calls);
            } else if (item.getItemId() == R.id.notesNavigationButton) {
                StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, 1);

                recyclerview.setHasFixedSize(true);
                recyclerview.setAdapter(notesAdapter);
                recyclerview.setLayoutManager(layoutManager);

                topAppBar.setTitle(R.string.notes);
            } else {
                return false;
            }
            selectedMenuItemInBotomAppBar = item.getItemId();
            return true;
        });
    }

    private void notesView() {
        FirestoreRecyclerOptions<Topics_Tile_Data> options = new FirestoreRecyclerOptions.Builder<Topics_Tile_Data>()
                .setQuery(noteQuery, Topics_Tile_Data.class).build();

        notesAdapter = new Notes_Adapter(options, getApplicationContext());
        notesAdapter.startListening();
    }

    private void callView() {
        callAdapter = new CallAdapter(data);
    }

    private void chatView() {
        FirestoreRecyclerOptions<ChatFragment_Tile_Data> options = new FirestoreRecyclerOptions.Builder<ChatFragment_Tile_Data>()
                .setQuery(chatQuery, ChatFragment_Tile_Data.class).build();

        chatFragmentAdapter = new ChatFragmentAdapter(options, this, currentUserId);
        chatFragmentAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        chatFragmentAdapter.stopListening();
        notesAdapter.stopListening();
    }
}