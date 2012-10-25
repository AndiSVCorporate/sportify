package com.msports.sportify.preferences;

import com.msports.sportify.android.R;

import android.content.Context;
import android.util.AttributeSet;

public class BodyHeightPreference extends EditMeasurementPreference {

	public BodyHeightPreference(Context context) {
		super(context);
	}
	public BodyHeightPreference(Context context, AttributeSet attr) {
		super(context, attr);
	}
	public BodyHeightPreference(Context context, AttributeSet attr, int defStyle) {
		super(context, attr, defStyle);
	}

	protected void initPreferenceDetails() {
		mTitleResource = R.string.body_height_setting_title;
		mMetricUnitsResource = R.string.centimeters;
	}
}

