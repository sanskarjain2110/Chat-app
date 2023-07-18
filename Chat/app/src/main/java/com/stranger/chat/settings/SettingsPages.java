package com.stranger.chat.settings;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.appbar.MaterialToolbar;
import com.stranger.chat.R;
import com.stranger.chat.settings.fregment.AccountPreferences;
import com.stranger.chat.settings.fregment.AppearancePreferences;

public class SettingsPages extends AppCompatActivity {
    Intent intent;

    MaterialToolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_page);
        intent = getIntent();

        toolbar = findViewById(R.id.toolbar);

        toolbar.setTitle(intent.getStringExtra("title"));
        toolbar.setNavigationOnClickListener(view -> finish());

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        int xml = intent.getIntExtra("xml",0);
        if (xml == R.xml.account_preferences) {
            fragmentTransaction.replace(R.id.settings, new AccountPreferences());
        } else if (xml == R.xml.apperrance_preferences) {
            fragmentTransaction.replace(R.id.settings, new AppearancePreferences());
            // else if

        } else throw new RuntimeException("Invalid xml");

        fragmentTransaction.commit();
    }
}
