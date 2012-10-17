package com.msports.sportify.android.sensors;

import java.util.Vector;

import android.content.Context;

import com.msports.sportify.android.sensors.steps.StepData;
import com.msports.sportify.android.sensors.steps.StepListener;
import com.msports.sportify.android.sensors.steps.StepSensor;

public class SensorFactoryManager implements StepListener{

	private static SensorFactoryManager manager;
	
	private StepSensor stepSensor;
	
	private Vector<StepListener> stepListeners;
	private Context context;
	
	private SensorFactoryManager(Context context) {
		stepListeners = new Vector<StepListener>();
		this.context = context;
	}
	
	public static SensorFactoryManager getInstance(Context context) {
		if(manager == null) {
			manager = new SensorFactoryManager(context);
		}
		return manager;
	}
	
	public void registerStepListener(StepListener stepListener) {
		if(stepSensor == null) {
			stepSensor = new StepSensor(context);
			stepSensor.setStepListener(this);
		}
		stepListeners.add(stepListener);
	}

	@Override
	public void onUpdateStepSensor(StepData _stepData) {
		for(StepListener listener: stepListeners) {
			listener.onUpdateStepSensor(_stepData);
		}
	}
	
}
