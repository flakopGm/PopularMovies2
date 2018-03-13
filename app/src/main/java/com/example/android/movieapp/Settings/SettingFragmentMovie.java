package com.example.android.movieapp.Settings;

import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;
import com.example.android.movieapp.R;

/**
 * Created by Joni on 12/03/2018.
 */

public class SettingFragmentMovie extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.settings_movies);
    }
}
