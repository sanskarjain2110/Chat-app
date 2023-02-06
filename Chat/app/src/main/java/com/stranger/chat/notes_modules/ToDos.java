package com.stranger.chat.notes_modules;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.stranger.chat.R;
import com.stranger.chat.notes_modules.adapter.Task_Adapter;
import com.stranger.chat.notes_modules.data.Task_Tile_Data;

public class ToDos extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    String currentUserId;

    CollectionReference todoCollectionReference;
    DocumentReference documentReference;
    Query todoQuery;
    String noteId;

    Toolbar topAppBar;
    RecyclerView listView;
    ExtendedFloatingActionButton addTask;

    Task_Adapter taskAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);

        // current user
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        currentUserId = currentUser.getUid();

        topAppBar = findViewById(R.id.topAppBar);
        listView = findViewById(R.id.listView);
        addTask = findViewById(R.id.addTask);

        noteId = getIntent().getStringExtra("noteId");

        documentReference = FirebaseFirestore.getInstance().collection("users")
                .document(currentUserId).collection("notes").document(noteId);

        documentReference.addSnapshotListener((value, error) -> {
            if (error != null) {
                return;
            }
            if (value != null && value.exists()) {
                topAppBar.setTitle((String) value.get("title"));
            }
        });

        topAppBar.setNavigationOnClickListener(view -> finish());
        topAppBar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.deleteList) {                                              // delete list
                documentReference.delete().addOnSuccessListener(unused -> finish());
            } else if (item.getItemId() == R.id.editListName) {                                      // edit list name
                Toast.makeText(getApplicationContext(), "yet to resolve", Toast.LENGTH_LONG).show();
            } else {
                return false;
            }
            return true;
        });

        addTask.setOnClickListener(v -> {
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        todoCollectionReference = documentReference.collection("todo");
        todoQuery = todoCollectionReference.orderBy("status");

        FirestoreRecyclerOptions<Task_Tile_Data> options = new FirestoreRecyclerOptions.Builder<Task_Tile_Data>().setQuery(todoQuery, Task_Tile_Data.class).build();

        taskAdapter = new Task_Adapter(options, todoCollectionReference);
        taskAdapter.startListening();

        listView.setAdapter(taskAdapter);
        listView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    }
}