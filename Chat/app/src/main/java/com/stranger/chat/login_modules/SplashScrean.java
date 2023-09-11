package com.stranger.chat.login_modules;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import com.stranger.chat.R;
import com.stranger.chat.fuctionality.Routes;
import com.stranger.chat.fuctionality.Theme;

public class SplashScrean extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        final String app_theme = sharedPreferences.getString(getString(R.string.app_theme), "System default");

        Theme.setTheme(app_theme);// set dark or light mode
        new Handler().postDelayed(() -> new Routes(this, this).loginModuleRoutes(),
                1000);
    }
}