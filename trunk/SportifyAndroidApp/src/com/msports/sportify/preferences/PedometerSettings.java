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
    
    public float getBodyHeight() { //in centimeters
        try {
            return Float.valueOf(mSettings.getString("body_height", "170").trim());
        }
        catch (NumberFormatException e) {
            return 0f;
        }
    }
    
    public int getRestingHeartRate() { //pulse
        try {
        	return Integer.valueOf(mSettings.getString("hrrest", "50").trim());
        }
        catch (NumberFormatException e) {
            return 0;
        }
    }

	public int getGender() {
		try {
        	return Integer.valueOf(mSettings.getString("gender", "1").trim());
        }
        catch (NumberFormatException e) {
            return 0;
        }
	}

	public int getAge() {
		try {
        	return Integer.valueOf(mSettings.getString("age", "30").trim());
        }
        catch (NumberFormatException e) {
            return 0;
        }
	}

	public int getPyhsicalActRating() {
		try {
            return Integer.valueOf(mSettings.getString("par", "4").trim());
        }
        catch (NumberFormatException e) {
            return 0;
        }
	}
}
