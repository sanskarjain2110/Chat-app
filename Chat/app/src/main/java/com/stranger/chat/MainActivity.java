package com.stranger.chat;

import static com.stranger.chat.fuctionality.FirebaseDatabaseConnection.currentUser;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.Query;
import com.stranger.chat.chat_modules.AddPerson;
import com.stranger.chat.chat_modules.adapter.ChatAdapter;
import com.stranger.chat.chat_modules.data.Chat_Tile_Data;
import com.stranger.chat.fuctionality.FirebaseDatabaseConnection;
import com.stranger.chat.notes_modules.adapter.NotesAdapter;
import com.stranger.chat.notes_modules.bottom_sheet.NotesCreate_BottomSheet;
import com.stranger.chat.notes_modules.data.Note_Tile_Data;
import com.stranger.chat.settings.Settings;

public class MainActivity extends AppCompatActivity {

    private final String currentUserId = currentUser.getUid();

    private final CollectionReference notesCollectionReference = FirebaseDatabaseConnection.notesCollectionReference(currentUserId);
    Query chatQuery, noteQuery;

    ChatAdapter chatAdapter;
    NotesAdapter notesAdapter;

    Toolbar topAppBar;
    RecyclerView recyclerview;

    FloatingActionButton addButton;
    BottomNavigationView bottomNavigationBar;

    int selectedMenuItemInBotomAppBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        topAppBar = findViewById(R.id.topAppBar);
        recyclerview = findViewById(R.id.recyclerview);

        addButton = findViewById(R.id.addButton);
        bottomNavigationBar = findViewById(R.id.bottom_navigation);

        selectedMenuItemInBotomAppBar = R.id.chatNavigationButton;

        topAppBar.setNavigationOnClickListener(item -> startActivity(new Intent(getApplicationContext(), Settings.class)));
        topAppBar.setSubtitle("Hello!");
        topAppBar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.settings) {
                startActivity(new Intent(getApplicationContext(), Settings.class));
            } else {
                return false;
            }
            return true;
        });

        addButton.setOnClickListener(view -> {
            if (selectedMenuItemInBotomAppBar == R.id.chatNavigationButton) {
                startActivity(new Intent(getApplicationContext(), AddPerson.class));
            } else if (selectedMenuItemInBotomAppBar == R.id.notesNavigationButton) {
                NotesCreate_BottomSheet bottomSheet = new NotesCreate_BottomSheet(getApplicationContext(), notesCollectionReference);
                bottomSheet.show(getSupportFragmentManager(), "ModalBottomSheet");
            } else {
                Toast.makeText(this, "error!!", Toast.LENGTH_SHORT).show();
            }
        });

        bottomNavigationBar.setOnItemSelectedListener(item -> navigationMenu(item.getItemId()));
    }

    @Override
    protected void onResume() {
        super.onResume();
        chatView();
        notesView();

        navigationMenu(selectedMenuItemInBotomAppBar);
    }

    @Override
    protected void onStop() {
        super.onStop();
        chatAdapter.stopListening();
        notesAdapter.stopListening();
    }

    private void notesView() {
        noteQuery = notesCollectionReference.orderBy("lastUpdated", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<Note_Tile_Data> options = new FirestoreRecyclerOptions.Builder<Note_Tile_Data>().setQuery(noteQuery, Note_Tile_Data.class).build();

        notesAdapter = new NotesAdapter(options, this, getSupportFragmentManager(), notesCollectionReference);
        notesAdapter.startListening();
    }

    private void chatView() {
        chatQuery = FirebaseDatabaseConnection.messageCollection.whereArrayContains("usersId", currentUserId);

        FirestoreRecyclerOptions<Chat_Tile_Data> options = new FirestoreRecyclerOptions.Builder<Chat_Tile_Data>()
                .setQuery(chatQuery, Chat_Tile_Data.class).build();

        chatAdapter = new ChatAdapter(options, this, currentUserId, getSupportFragmentManager());
        chatAdapter.startListening();
    }

    protected boolean navigationMenu(int item) {
        if (item == R.id.chatNavigationButton) {
            recyclerview.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            recyclerview.setAdapter(chatAdapter);
        } else if (item == R.id.notesNavigationButton) {
            StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, 1);

            recyclerview.setHasFixedSize(true);
            recyclerview.setAdapter(notesAdapter);
            recyclerview.setLayoutManager(layoutManager);
        } else {
            return false;
        }
        selectedMenuItemInBotomAppBar = item;
        return true;
    }
}

