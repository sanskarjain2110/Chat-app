package com.stranger.chat;

import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    FrameLayout mainScreen;
    TextView title;
    BottomNavigationView navigationBarMenu;

    Fragment chatFragment = new ChatFragment(),
            callFragment = new CallFragment();

    String[] titleText = {"Chat", "Call"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        title = findViewById(R.id.title);
        mainScreen = findViewById(R.id.mainScreen);
        getSupportFragmentManager().beginTransaction().replace(R.id.mainScreen, chatFragment, null).commit();

        navigationBarMenu = findViewById(R.id.bottom_navigation);
        navigationBarMenu.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.chatNavigationButton:
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.mainScreen, chatFragment, null).commit();
                    return true;
                case R.id.callNavigationButton:
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.mainScreen, callFragment, null).commit();
                    return true;
                default:
                    return false;
            }
        });


    }
}