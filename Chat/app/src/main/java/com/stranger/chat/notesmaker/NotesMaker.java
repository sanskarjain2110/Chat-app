package com.stranger.chat.notesmaker;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.stranger.chat.R;
import com.stranger.chat.notesmaker.contentPage.ContentActivity;

import java.util.HashMap;
import java.util.Map;

public class NotesMaker extends AppCompatActivity {
    RecyclerView recyclerView;

    Topics_Adapter adapter;

    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    String currentUserId;

    FirebaseFirestore database;
    CollectionReference reference;
    Query query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_maker);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        currentUserId = currentUser.getUid();

        database = FirebaseFirestore.getInstance();
        reference = database.collection("users").document(currentUserId).collection("notes");
        query = reference;
        //        .orderBy("lastUpdated");

        recyclerView = findViewById(R.id.main_RecyclerView);

        // add floating button
        findViewById(R.id.addButton).setOnClickListener(view1 -> {

            String key = reference.document().getId();

            Map<String, Object> data = new HashMap<>();
            data.put("topicId", key);
            data.put("topic", key);

            reference.document(key).set(data)
                    .addOnSuccessListener(unused -> {
                        Intent intent = new Intent(getApplicationContext(), ContentActivity.class);
                        intent.putExtra("topicId", key);
                        startActivity(intent);
                    }).addOnFailureListener(e -> Toast.makeText(getApplicationContext(), e + " ", Toast.LENGTH_SHORT).show());
        });

        query.addSnapshotListener((snapshot, error) -> {
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        FirestoreRecyclerOptions<Topics_Tile_Data> options = new FirestoreRecyclerOptions.Builder<Topics_Tile_Data>()
                .setQuery(query, Topics_Tile_Data.class).build();

        adapter = new Topics_Adapter(options, this, getApplicationContext());
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}