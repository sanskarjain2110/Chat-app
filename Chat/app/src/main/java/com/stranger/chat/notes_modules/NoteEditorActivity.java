package com.stranger.chat.notes_modules;

import static android.text.TextUtils.isEmpty;
import static com.stranger.chat.fuctionality.TimeStamp.timeStamp;
import static java.util.Objects.isNull;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.stranger.chat.R;

import java.util.HashMap;
import java.util.Map;

public class NoteEditorActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    String currentUserId;

    CollectionReference collectionReference;
    DocumentReference documentReference;
    String noteId;

    Toolbar topAppBar;

    TextView titleTextView, descriptionTextView;
    Boolean updateNotes = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_editor);

        // current user
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        currentUserId = currentUser.getUid();

        topAppBar = findViewById(R.id.topAppBar);

        titleTextView = findViewById(R.id.title);
        descriptionTextView = findViewById(R.id.description);

        collectionReference = FirebaseFirestore.getInstance().collection("users").document(currentUserId).collection("notes");

        noteId = getIntent().getStringExtra("noteId");
        if (isNull(noteId)) {
            noteId = collectionReference.document().getId();
            updateNotes = false;
        } else {
            collectionReference.document(noteId).addSnapshotListener((value, error) -> {
                if (error != null) {
                    return;
                }
                if (value != null && value.exists()) {
                    titleTextView.setText((String) value.get("title"));
                    descriptionTextView.setText((String) value.get("description"));
                }
            });
        }

        documentReference = collectionReference.document(noteId);

        //  back button
        topAppBar.setNavigationOnClickListener(view -> finish());

        topAppBar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.save) {
                String title = titleTextView.getText().toString().trim();
                if (!isEmpty(title)) {
                    String description = descriptionTextView.getText().toString().trim();

                    Map<String, Object> data = new HashMap<>();
                    data.put("title", title);
                    data.put("noteId", noteId);
                    data.put("description", description);
                    data.put("lastUpdated", timeStamp());
                    if (updateNotes) {
                        documentReference.update(data).addOnSuccessListener(unused -> finish());
                    } else {
                        documentReference.set(data).addOnSuccessListener(unused -> finish());
                    }
                } else {
                    Toast.makeText(this, "Plesae set the title", Toast.LENGTH_SHORT).show();
                }
            } else if (item.getItemId() == R.id.delete && updateNotes) {
                documentReference.delete().addOnSuccessListener(unused -> finish());
            } else {
                return false;
            }
            return true;
        });
    }
}