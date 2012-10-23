package com.msports.sportify.preferences;

import com.msports.sportify.android.R;

import android.content.Context;
import android.util.AttributeSet;

public class RestingHeartRatePreference extends EditMeasurementPreference {

	public RestingHeartRatePreference(Context context) {
		super(context);
	}
	public RestingHeartRatePreference(Context context, AttributeSet attr) {
		super(context, attr);
	}
	public RestingHeartRatePreference(Context context, AttributeSet attr, int defStyle) {
		super(context, attr, defStyle);
	}

	protected void initPreferenceDetails() {
		mTitleResource = R.string.hrrest_setting_title;
		mMetricUnitsResource = R.string.pulse;
	}
}

