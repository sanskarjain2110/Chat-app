package com.stranger.chat.settings;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;
import com.stranger.chat.R;
import com.stranger.chat.login_modules.Log_in_page;

public class Settings extends AppCompatActivity {
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    String currentUserId;

    Toolbar topAppBar;
    CardView userCardView;
    ImageView profilePic;
    TextView username, phoneNumber;
    Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        currentUserId = currentUser.getUid();

        topAppBar = findViewById(R.id.topAppBar);
        userCardView = findViewById(R.id.userCard);
        profilePic = findViewById(R.id.profilePic);
        username = findViewById(R.id.username);
        phoneNumber = findViewById(R.id.phoneNumber);
        loginButton = findViewById(R.id.logout);

        topAppBar.setNavigationOnClickListener(v -> finish());

        userCardView.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), Profile_Update.class)));

        loginButton.setOnClickListener(view -> {
            FirebaseAuth.getInstance().signOut();

            startActivity(new Intent(getApplicationContext(), Log_in_page.class));
            finish();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        FirebaseFirestore.getInstance().collection("users").document(currentUserId).addSnapshotListener((value, error) -> {
            if (error != null) {
                return;
            }
            if (value != null) {
                phoneNumber.setText((String) value.get("phoneNumber"));
                username.setText((String) value.get("username"));
                if (value.get("profilePic") != null) {
                    Picasso.get().load((String) value.get("profilePic")).into(profilePic);
                }
            }
        });
    }
}