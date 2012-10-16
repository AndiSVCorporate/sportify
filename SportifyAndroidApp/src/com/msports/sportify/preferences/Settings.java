package com.msports.sportify.preferences;

import com.msports.sportify.android.R;

import android.os.Bundle;
import android.preference.PreferenceActivity;


public class Settings extends PreferenceActivity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        addPreferencesFromResource(R.xml.preferences);
    }
}
