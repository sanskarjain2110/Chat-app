package com.stranger.chat;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.stranger.chat.login.Log_in_page;

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

        Button logout = findViewById(R.id.logout);
        logout.setOnClickListener(view -> {
            FirebaseAuth.getInstance().signOut();

            startActivity(new Intent(getApplicationContext(), Log_in_page.class));
            finish();
        });


    }
}