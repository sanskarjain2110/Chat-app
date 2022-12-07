package com.stranger.chat.settings;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.stranger.chat.R;
import com.stranger.chat.login_modules.Log_in_page;

public class Settings_MainPage extends AppCompatActivity {
    Button updateProfileButton, loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_main_page);

        loginButton = findViewById(R.id.logout);

        // update button
        findViewById(R.id.updateProfile).setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), Profile_Update.class)));

        loginButton.setOnClickListener(view -> {
            FirebaseAuth.getInstance().signOut();

            startActivity(new Intent(getApplicationContext(), Log_in_page.class));
            finish();
        });
    }
}