package com.msports.sportify.android.sensors;

import java.util.Vector;

import android.content.Context;

import com.msports.sportify.android.sensors.heartRate.AntHeartRateSensor;
import com.msports.sportify.android.sensors.heartRate.HeartRateData;
import com.msports.sportify.android.sensors.heartRate.HeartRateListener;
import com.msports.sportify.android.sensors.heartRate.SimulationHeartRateSensor;
import com.msports.sportify.android.sensors.steps.StepData;
import com.msports.sportify.android.sensors.steps.StepListener;
import com.msports.sportify.android.sensors.steps.StepSensor;

public class SensorFactoryManager implements StepListener, HeartRateListener{

	private static SensorFactoryManager manager;
	
	private StepSensor stepSensor;
	private SimulationHeartRateSensor simulationHeartRateSensor;
	private AntHeartRateSensor antHeartRateSensor;
	
	private Vector<StepListener> stepListeners;
	private Vector<HeartRateListener> heartRateListeners;
	private Context context;
	
	private SensorFactoryManager(Context context) {
		stepListeners = new Vector<StepListener>();
		heartRateListeners = new Vector<HeartRateListener>();
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
	
	public void registerSimulationHeartRateListener(HeartRateListener heartRateListener) {
		if(simulationHeartRateSensor == null) {
			simulationHeartRateSensor = new SimulationHeartRateSensor(this);
			simulationHeartRateSensor.connect();
		}
		heartRateListeners.add(heartRateListener);
	}
	
	public void registerAntHeartRateListener(HeartRateListener heartRateListener) {
		if(antHeartRateSensor == null) {
			antHeartRateSensor = new AntHeartRateSensor(context, this);
			antHeartRateSensor.connect();
		}
		heartRateListeners.add(heartRateListener);
	}

	@Override
	public void onUpdateStepSensor(StepData _stepData) {
		for(StepListener listener: stepListeners) {
			listener.onUpdateStepSensor(_stepData);
		}
	}

	@Override
	public void onUpdateHeartRateSensorData(HeartRateData data) {
		for(HeartRateListener listener: heartRateListeners) {
			listener.onUpdateHeartRateSensorData(data);
		}
	}
	
}
