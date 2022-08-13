package com.stranger.chat.data;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Firebase extends AppCompatActivity {
    public Firebase() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> user1 = new HashMap<>();
        user1.put("emailId", "Ada");

        Map<String, Object> user = new HashMap<>();
        user.put("emailId", user1);

        db.collection("users")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(getApplicationContext(), "Please check Your Internet Connection", Toast.LENGTH_SHORT).show());
    }
}
