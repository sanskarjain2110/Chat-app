package com.stranger.chat.login_modules;

import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.stranger.chat.R;
import com.stranger.chat.fuctionality.Routes;

public class SplashScrean extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(() -> new Routes(this, this).loginModuleRoutes(),
                1000);
    }
}