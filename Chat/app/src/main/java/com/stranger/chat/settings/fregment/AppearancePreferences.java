package com.stranger.chat.settings.fregment;

import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.stranger.chat.R;
import com.stranger.chat.fuctionality.Theme;

public class AppearancePreferences extends PreferenceFragmentCompat {

    Context context;
    public final int THEME_PREFERENCE = R.string.app_theme;
    public final int COLOR_AND_WALLPAPER_PREFERANCE = R.string.color_and_wallpaper_preferance;
    public final int FONT_SIZE_PREFERANCE = R.string.font_size_preferance;

    private Preference themePreference;
    private Preference fontSizePreferance;


    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.apperrance_preferences, rootKey);

        context = getContext();
        themePreference = findPreference(getString(THEME_PREFERENCE));
        fontSizePreferance = findPreference(getString(FONT_SIZE_PREFERANCE));

        themePreference.setOnPreferenceChangeListener((preference, newValue) -> Theme.setTheme(newValue));

        fontSizePreferance.setOnPreferenceChangeListener(((preference, newValue) -> {
            Toast.makeText(context, "" + newValue, Toast.LENGTH_SHORT).show();

            return true;
        }));
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
