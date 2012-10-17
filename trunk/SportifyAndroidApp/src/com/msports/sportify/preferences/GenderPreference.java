package com.msports.sportify.preferences;

import com.msports.sportify.android.R;

import android.content.Context;
import android.util.AttributeSet;

public class GenderPreference extends EditMeasurementPreference {

	public GenderPreference(Context context) {
		super(context);
	}
	public GenderPreference(Context context, AttributeSet attr) {
		super(context, attr);
	}
	public GenderPreference(Context context, AttributeSet attr, int defStyle) {
		super(context, attr, defStyle);
	}

	protected void initPreferenceDetails() {
		mTitleResource = R.string.gender_setting_title;
		mMetricUnitsResource = R.string.gender;
	}
}

