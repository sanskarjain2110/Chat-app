package com.stranger.chat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
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
import com.stranger.chat.notes_modules.NoteEditorActivity;
import com.stranger.chat.notes_modules.adapter.Notes_Adapter;
import com.stranger.chat.notes_modules.data.Note_Tile_Data;
import com.stranger.chat.settings.Settings;

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
            new Userdata(R.drawable.ic_launcher_background, R.drawable.ic_launcher_background, R.drawable.ic_launcher_background, "Rohan", "20/07/2022, 07:30 a.m.")
    };

    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    String currentUserId;

    CollectionReference chatReference, notesReference;
    Query chatQuery, noteQuery;

    ChatFragmentAdapter chatFragmentAdapter;
    CallAdapter callAdapter;
    Notes_Adapter notesAdapter;

    Toolbar topAppBar;

    RecyclerView recyclerview;

    LinearLayout notesNavigation;
    FloatingActionButton createNoteButton, createListButton;

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

        notesReference = FirebaseFirestore.getInstance().collection("users").document(currentUserId).collection("notes");
        noteQuery = notesReference.orderBy("lastUpdated", Query.Direction.DESCENDING);
        chatReference = FirebaseFirestore.getInstance().collection("messages");
        chatQuery = chatReference.whereArrayContains("usersId", currentUserId).orderBy("lastSeen");

        topAppBar = findViewById(R.id.topAppBar);

        recyclerview = findViewById(R.id.recyclerview);

        notesNavigation = findViewById(R.id.notesNavigation);
        createNoteButton = findViewById(R.id.createNoteButton);
        createListButton = findViewById(R.id.createListButton);

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

        notesNavigation.setOnClickListener(view -> notesNavigation.setVisibility(View.GONE));

        addButton.setOnClickListener(view -> {
            if (selectedMenuItemInBotomAppBar == R.id.chatNavigationButton) {
                startActivity(new Intent(getApplicationContext(), AddChat.class));
            } else if (selectedMenuItemInBotomAppBar == R.id.callNavigationButton) {
                Toast.makeText(this, "hello", Toast.LENGTH_SHORT).show();
            } else if (selectedMenuItemInBotomAppBar == R.id.notesNavigationButton) {
                notesNavigation.setVisibility(View.VISIBLE);
            } else {
                Toast.makeText(this, "There is an error", Toast.LENGTH_SHORT).show();
            }
        });

        createNoteButton.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), NoteEditorActivity.class)));

        bottomAppBar.setOnMenuItemClickListener(item -> navigationMenu(item.getItemId()));
    }

    @Override
    protected void onResume() {
        super.onResume();
        chatView();
        callView();
        notesView();

        navigationMenu(selectedMenuItemInBotomAppBar);
    }

    @Override
    protected void onStop() {
        super.onStop();
        chatFragmentAdapter.stopListening();
        notesAdapter.stopListening();
    }

    private void notesView() {
        FirestoreRecyclerOptions<Note_Tile_Data> options = new FirestoreRecyclerOptions.Builder<Note_Tile_Data>()
                .setQuery(noteQuery, Note_Tile_Data.class).build();

        notesAdapter = new Notes_Adapter(options, this);
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

    protected boolean navigationMenu(int item) {
        if (item == R.id.chatNavigationButton) {
            recyclerview.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            recyclerview.setAdapter(chatFragmentAdapter);

            topAppBar.setTitle(R.string.chats);
        } else if (item == R.id.callNavigationButton) {
            recyclerview.setAdapter(callAdapter);
            recyclerview.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

            topAppBar.setTitle(R.string.calls);
        } else if (item == R.id.notesNavigationButton) {
            StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, 1);

            recyclerview.setHasFixedSize(true);
            recyclerview.setAdapter(notesAdapter);
            recyclerview.setLayoutManager(layoutManager);

            topAppBar.setTitle(R.string.notes);
        } else {
            return false;
        }
        selectedMenuItemInBotomAppBar = item;
        return true;
    }
}