package com.msports.sportify.android.session;

import com.msports.sportify.android.MainActivity;
import com.msports.sportify.android.model.SessionModel;
import com.msports.sportify.android.sensors.SensorFactoryManager;
import com.msports.sportify.android.sensors.heartRate.HeartRateData;
import com.msports.sportify.android.sensors.heartRate.HeartRateListener;
import com.msports.sportify.android.sensors.steps.StepData;
import com.msports.sportify.android.sensors.steps.StepListener;

public class SessionManager implements HeartRateListener, StepListener{

	public MainActivity mainActivity;
	
	private SessionModel model;
	
	public SessionManager(MainActivity mainActivity) {
		this.mainActivity = mainActivity;
		model = new SessionModel();
		SensorFactoryManager sensorFactoryManager = SensorFactoryManager.getInstance(mainActivity);
		sensorFactoryManager.registerSimulationHeartRateListener(this);
		sensorFactoryManager.registerStepListener(this);
	}

	@Override
	synchronized public void onUpdateHeartRateSensorData(HeartRateData data) {
		model.setCurrentHeartRate(data.getHeartRate());
		mainActivity.updateViews(model);
	}

	@Override
	public void onUpdateStepSensor(StepData _data) {
		model.setSpeed(_data.getM_speed());
		model.setDistance(_data.getM_distance());
		mainActivity.updateViews(model);
	}
	
	
	
}
