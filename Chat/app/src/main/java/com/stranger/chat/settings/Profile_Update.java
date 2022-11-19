package com.stranger.chat.settings;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;
import com.stranger.chat.R;

public class Profile_Update extends AppCompatActivity {

    ImageView profilePicField, backButton;
    EditText nameField;
    TextView phoneNumberField;
    Button updateButton;

    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_update);

        profilePicField = findViewById(R.id.changeProfilePic);
        backButton = findViewById(R.id.backButton);

        nameField = findViewById(R.id.nameField);

        phoneNumberField = findViewById(R.id.phoneNumberField);

        updateButton = findViewById(R.id.updateButton);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        currentUserId = currentUser.getUid();

        FirebaseFirestore.getInstance().collection("users").document(currentUserId).addSnapshotListener((value, error) -> {
            if (error != null) {
                return;
            }
            if (value != null) {
                nameField.setText((String) value.get("username"));
                phoneNumberField.setText((String) value.get("phonenumber"));
                if (value.get("profilePic") != null) {
                    Picasso.get().load((Uri) value.get("profilePic")).into(profilePicField);
                }
            }
        });

        profilePicField.setOnClickListener(view -> {
            Intent openGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(openGalleryIntent, 101);
        });
    }
}