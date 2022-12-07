package com.stranger.chat.notesmaker.contentPage;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.stranger.chat.R;

import java.util.HashMap;
import java.util.Map;

public class ContentActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    String currentUserId;

    FirebaseFirestore database;
    DocumentReference reference;

    TextView topicTextView, contentTextView;
    String topicId, topic, content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);

        // intent
        topicId = getIntent().getStringExtra("topicId");

        // current user
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        currentUserId = currentUser.getUid();

        database = FirebaseFirestore.getInstance();
        reference = database.collection("users").document(currentUserId).collection("notes").document(topicId);

        topicTextView = findViewById(R.id.topic);
        contentTextView = findViewById(R.id.content);

        reference.addSnapshotListener((snapshot, error) -> {
            if (error != null) {
                return;
            }
            if (snapshot != null) {
                topic = (String) snapshot.get("topic");
                content = (String) snapshot.get("content");

                topicTextView.setText(topic);
                contentTextView.setText(content);
            }
        });

        //  back button
        findViewById(R.id.backButton).setOnClickListener(v -> finish());

        // saveButton
        findViewById(R.id.saveButton).setOnClickListener(v -> {
            topic = topicTextView.getText().toString().trim();
            content = contentTextView.getText().toString().trim();

            Map<String, Object> data = new HashMap<>();
            data.put("topic", topic);
            data.put("content", content);
            // time stamp

            reference.update(data).addOnSuccessListener(unused -> finish());
        });
    }
}