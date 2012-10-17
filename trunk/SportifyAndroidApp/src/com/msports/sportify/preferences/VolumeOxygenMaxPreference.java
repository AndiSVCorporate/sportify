package com.msports.sportify.preferences;

import com.msports.sportify.android.R;

import android.content.Context;
import android.util.AttributeSet;

public class VolumeOxygenMaxPreference extends EditMeasurementPreference {

	public VolumeOxygenMaxPreference(Context context) {
		super(context);
	}
	public VolumeOxygenMaxPreference(Context context, AttributeSet attr) {
		super(context, attr);
	}
	public VolumeOxygenMaxPreference(Context context, AttributeSet attr, int defStyle) {
		super(context, attr, defStyle);
	}

	protected void initPreferenceDetails() {
		mTitleResource = R.string.vomax_setting_title;
		mMetricUnitsResource = R.string.vomax;
	}
}

