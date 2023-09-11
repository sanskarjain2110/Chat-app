package com.stranger.chat.fuctionality;

import androidx.appcompat.app.AppCompatDelegate;

public class Theme {
    public static Boolean setTheme(Object theme) {

        if (theme.equals("Light")) AppCompatDelegate.setDefaultNightMode(AppCompatDelegate
                .MODE_NIGHT_NO);
        else if (theme.equals("Dark")) AppCompatDelegate.setDefaultNightMode(AppCompatDelegate
                .MODE_NIGHT_YES);
        else AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        return true;
    }
}
