package com.stranger.chat.data;

import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

public class FirebaseTransaction extends AppCompatActivity {
    public FirebaseTransaction(Object data, String path) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection(path).add(data)
                .addOnFailureListener(e -> {
                    Toast.makeText(getApplicationContext(), "Please check Your Internet Connection", Toast.LENGTH_SHORT).show();
                    new FirebaseTransaction(data, path);
                });
    }
}
