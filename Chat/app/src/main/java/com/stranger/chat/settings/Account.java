package com.stranger.chat.settings;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.stranger.chat.R;

public class Account extends AppCompatActivity {

    Toolbar toolBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        toolBar = findViewById(R.id.toolBar);

        toolBar.setNavigationOnClickListener(view -> finish());
    }
}