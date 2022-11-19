package com.stranger.chat;

import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.stranger.chat.chat_modules.ChatFragment;
import com.stranger.chat.settings.Settings_MainPage;

public class MainActivity extends AppCompatActivity {
    FrameLayout mainScreen;
    BottomNavigationView navigationBarMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainScreen = findViewById(R.id.mainScreen);

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
                case R.id.settingsNavigationButton:
                    startActivity(new Intent(getApplicationContext(), Settings_MainPage.class));
                default:
                    return false;
            }
        });
    }
}