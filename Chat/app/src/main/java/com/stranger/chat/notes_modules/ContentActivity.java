package com.stranger.chat.notes_modules;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.stranger.chat.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ContentActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    String currentUserId;

    DocumentReference reference;

    Toolbar topAppBar;

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

        reference = FirebaseFirestore.getInstance().collection("users").document(currentUserId).collection("notes").document(topicId);

        topAppBar = findViewById(R.id.topAppBar);

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
        topAppBar.setNavigationOnClickListener(v -> {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss", Locale.US);
            String currentDateandTime = sdf.format(new Date());

            // change validation is not implemented
            Map<String, Object> data = new HashMap<>();
            data.put("lastUpdated", currentDateandTime);

            reference.update(data);
            finish();
        });

        topAppBar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.save) {
                topic = topicTextView.getText().toString().trim();
                content = contentTextView.getText().toString().trim();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
                String currentDateandTime = sdf.format(new Date());

                Map<String, Object> data = new HashMap<>();
                data.put("topic", topic);
                data.put("content", content);
                data.put("lastUpdated", currentDateandTime);

                reference.update(data).addOnSuccessListener(unused -> finish());
            } else {
                return false;
            }
            return true;
        });
    }
}