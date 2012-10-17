package com.msports.sportify.android;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class StepsDetector1 implements SensorEventListener{

	private Activity activity;
	
	private SensorManager sensorManager;
	private Sensor accelerometer;
	
	public StepsDetector1(Activity activity) {
		this.activity = activity;
		activity.getSystemService(activity.SENSOR_SERVICE);
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		
	}
	
	
}
