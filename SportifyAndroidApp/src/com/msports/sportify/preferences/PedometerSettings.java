package com.msports.sportify.preferences;

import android.content.SharedPreferences;

public class PedometerSettings {

    SharedPreferences mSettings;
    
    public PedometerSettings(SharedPreferences settings) {
        mSettings = settings;
    }
    
    public float getStepLength() {
        try {
            return Float.valueOf(mSettings.getString("step_length", "50").trim());
        }
        catch (NumberFormatException e) {
            return 0f;
        }
    }
    
    public float getBodyWeight() {
        try {
            return Float.valueOf(mSettings.getString("body_weight", "50").trim());
        }
        catch (NumberFormatException e) {
            return 0f;
        }
    }
}
