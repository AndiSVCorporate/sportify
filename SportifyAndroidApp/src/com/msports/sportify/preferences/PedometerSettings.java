package com.msports.sportify.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PedometerSettings {

    SharedPreferences mSettings;
    
    public PedometerSettings(Context context) {
        mSettings = PreferenceManager.getDefaultSharedPreferences(context);
    }
    
    public float getStepLength() { //in centimeter
        try {
            return Float.valueOf(mSettings.getString("step_length", "50").trim());
        }
        catch (NumberFormatException e) {
            return 0f;
        }
    }
    
    public float getBodyWeight() { //in kilograms
        try {
            return Float.valueOf(mSettings.getString("body_weight", "50").trim());
        }
        catch (NumberFormatException e) {
            return 0f;
        }
    }
}
