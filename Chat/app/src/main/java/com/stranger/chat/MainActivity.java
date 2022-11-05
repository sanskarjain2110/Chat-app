package com.stranger.chat;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.stranger.chat.chat_modules.ChatFragment;
import com.stranger.chat.login_modules.Log_in_page;

public class MainActivity extends AppCompatActivity {
    FrameLayout mainScreen;
    BottomNavigationView navigationBarMenu;
    Button logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainScreen = findViewById(R.id.mainScreen);
        logout = findViewById(R.id.logout);
        navigationBarMenu = findViewById(R.id.bottom_navigation);

        getSupportFragmentManager().beginTransaction().replace(R.id.mainScreen, new ChatFragment(), null).commit();

        navigationBarMenu.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.chatNavigationButton:
                    getSupportFragmentManager().beginTransaction().replace(R.id.mainScreen, new ChatFragment(), null).commit();
                    return true;
                case R.id.callNavigationButton:
                    getSupportFragmentManager().beginTransaction().replace(R.id.mainScreen, new CallFragment(), null).commit();
                    return true;
                default:
                    return false;
            }
        });

        logout.setOnClickListener(view -> {
            FirebaseAuth.getInstance().signOut();

            startActivity(new Intent(getApplicationContext(), Log_in_page.class));
            finish();
        });
    }
}