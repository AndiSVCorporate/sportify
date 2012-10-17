package com.msports.sportify.preferences;

import com.msports.sportify.android.R;

import android.content.Context;
import android.util.AttributeSet;

public class AgePreference extends EditMeasurementPreference {

	public AgePreference(Context context) {
		super(context);
	}
	public AgePreference(Context context, AttributeSet attr) {
		super(context, attr);
	}
	public AgePreference(Context context, AttributeSet attr, int defStyle) {
		super(context, attr, defStyle);
	}

	protected void initPreferenceDetails() {
		mTitleResource = R.string.age_setting_title;
		mMetricUnitsResource = R.string.years;
	}
}

