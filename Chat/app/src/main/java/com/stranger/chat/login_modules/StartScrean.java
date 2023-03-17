package com.stranger.chat.login_modules;

import static com.stranger.chat.fuctionality.FirebaseDatabaseConnection.currentUser;
import static com.stranger.chat.fuctionality.FirebaseDatabaseConnection.userDocument;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;


import androidx.appcompat.app.AppCompatActivity;

import com.stranger.chat.MainActivity;
import com.stranger.chat.R;

public class StartScrean extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);

        new Handler().postDelayed(() -> {
            if (currentUser != null) {
                userDocument(currentUser.getUid()).addSnapshotListener((snapshot, error) -> {
                    if (error != null) {
                        return;
                    }
                    if (snapshot != null) {
                        if (snapshot.exists()) {
                            startActivity(new Intent(StartScrean.this, MainActivity.class));
                        } else {
                            startActivity(new Intent(StartScrean.this, Add_Profile_Detail.class));
                        }
                    }
                });
            } else {
                startActivity(new Intent(StartScrean.this, LogInPage.class));
            }
            finish();
        }, 3000);
    }
}