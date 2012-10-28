package com.msports.sportify.android.model;

import gueei.binding.Command;
import gueei.binding.Observable;

import java.util.Vector;



import android.view.View;

import com.msports.sportify.android.sensors.heartRate.HeartRateData;
import com.msports.sportify.android.session.SessionManager;

public class SessionModel {

	public Observable<Long> startTime;
	public Observable<Long> duration;
	
	public Observable<Integer> temperature;
	public Observable<Integer> condition;
	
	public Observable<Integer> avgHeartRate;
	public Observable<Integer> maxHeartRate;
	public Observable<Integer> currentPercentageOfMaxHeartRate;
	public Observable<Integer> currentHeartRate;
	public Vector<HeartRateData> heartRateTrace;
	
	public Observable<Float> distance; 
	public Observable<Float> speed;
	
	public Observable<Float> calories;
	
	public Observable<Float> trimpScore;
	
	public Command startSession, stopSession;
	
	public SessionManager sessionManager;

	public SessionModel(SessionManager sessionManager) {
		startTime = new Observable<Long>(Long.class, 0l);
		duration = new Observable<Long>(Long.class, 0l);
		temperature = new Observable<Integer>(Integer.class, Integer.MIN_VALUE);
		condition = new Observable<Integer>(Integer.class, -1);
		avgHeartRate = new Observable<Integer>(Integer.class, 0);
		maxHeartRate = new Observable<Integer>(Integer.class, 0);
		currentPercentageOfMaxHeartRate = new Observable<Integer>(Integer.class, 0);
		currentHeartRate = new Observable<Integer>(Integer.class, -1);
		distance = new Observable<Float>(Float.class, 0f);
		speed = new Observable<Float>(Float.class, 0f);
		calories = new Observable<Float>(Float.class, 0f);
		trimpScore = new Observable<Float>(Float.class, 0f);
		heartRateTrace = new Vector<HeartRateData>();
		
		this.sessionManager = sessionManager;
		
		initCommands();
		resetValues();
	}
	
	public void initCommands() {
		startSession = new Command() {
			
			@Override
			public void Invoke(View arg0, Object... arg1) {
				// TODO Auto-generated method stub
				
			}
		};
	}
	
	private static SessionModel instance;
	
	public static SessionModel getInstance(SessionManager manager) {
		if(instance == null) {
			instance = new SessionModel(manager);
		}
		return instance;
	}
	
	public void resetValues() {
		startTime.set(0l);
		duration.set(0l);
		temperature.set(-1);
		condition.set(-1);
		avgHeartRate.set(-1);
		maxHeartRate.set(-1);
		currentHeartRate.set(-1);
		currentPercentageOfMaxHeartRate.set(-1);
		trimpScore.set(0f);
		calories.set(0f);
		distance.set(0f);
		speed.set(0f);
		if(heartRateTrace != null) {
			heartRateTrace.clear();			
		}
	}
	
	
	
}
