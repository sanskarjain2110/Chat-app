package com.stranger.chat.settings.fregment;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import com.stranger.chat.R;

public class  AccountPreferences extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.account_preferences, rootKey);
    }
}