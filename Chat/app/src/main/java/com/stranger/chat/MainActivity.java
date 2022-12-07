package com.stranger.chat;

import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;
import com.stranger.chat.call_module.CallFragment;
import com.stranger.chat.chat_modules.ChatFragment;
import com.stranger.chat.notesmaker.NotesMaker;
import com.stranger.chat.settings.Settings_MainPage;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    String currentUserId;

    FrameLayout mainScreen;
    BottomNavigationView navigationBarMenu;
    ImageView settingsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        currentUserId = currentUser.getUid();

        mainScreen = findViewById(R.id.mainScreen);
        navigationBarMenu = findViewById(R.id.bottom_navigation);

        settingsButton = findViewById(R.id.settingsButton);

        // fill the details
        FirebaseFirestore.getInstance().collection("users").document(currentUserId).addSnapshotListener((value, error) -> {
            if (error != null) {
                return;
            }
            if (value != null) {
                if (value.get("profilePic") != null) {
                    Picasso.get().load((String) value.get("profilePic")).into(settingsButton);
                }
            }
        });


        settingsButton.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), Settings_MainPage.class)));

        getSupportFragmentManager().beginTransaction().replace(R.id.mainScreen, new ChatFragment(), null).commit();

        navigationBarMenu.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.chatNavigationButton:
                    getSupportFragmentManager().beginTransaction().replace(R.id.mainScreen, new ChatFragment(), null).commit();
                    return true;
                case R.id.callNavigationButton:
                    getSupportFragmentManager().beginTransaction().replace(R.id.mainScreen, new CallFragment(), null).commit();
                    return true;
                case R.id.notesMakerNavigationButton:
                    startActivity(new Intent(getApplicationContext(), NotesMaker.class));
                    return true;
                default:
                    return false;
            }
        });
    }

}