package com.msports.sportify.android.model;

import java.util.Vector;

import com.msports.sportify.android.sensors.heartRate.HeartRateData;

public class SessionModel {

	private long startTime;
	private long duration;
	
	private int temperature;
	private int condition;
	
	private int avgHeartRate;
	private int maxHeartRate;
	private int currentPercentageOfMaxHeartRate;
	private int currentHeartRate;
	private Vector<HeartRateData> heartRateTrace;
	
	private float distance; 
	private float speed;
	
	private float calories;
	
	private float trimpScore;

	public SessionModel() {
		
		resetValues();
	}
	
	public void resetValues() {
		startTime = -1;
		duration = 0;
		temperature = -1;
		condition = -1;
		avgHeartRate = -1;
		maxHeartRate = -1;
		currentHeartRate = -1;
		currentPercentageOfMaxHeartRate = -1;
		trimpScore = 0;
		calories = 0;
		distance = -1;
		speed = -1;
		if(heartRateTrace != null) {
			heartRateTrace.clear();			
		}
	}
	
	public int getAvgHeartRate() {
		return avgHeartRate;
	}

	public void setAvgHeartRate(int avgHeartRate) {
		this.avgHeartRate = avgHeartRate;
	}

	public int getMaxHeartRate() {
		return maxHeartRate;
	}

	public void setMaxHeartRate(int maxHeartRate) {
		this.maxHeartRate = maxHeartRate;
	}

	public int getCurrentPercentageOfMaxHeartRate() {
		return currentPercentageOfMaxHeartRate;
	}

	public void setCurrentPercentageOfMaxHeartRate(
			int currentPercentageOfMaxHeartRate) {
		this.currentPercentageOfMaxHeartRate = currentPercentageOfMaxHeartRate;
	}

	public int getCurrentHeartRate() {
		return currentHeartRate;
	}

	public void setCurrentHeartRate(int currentHeartRate) {
		this.currentHeartRate = currentHeartRate;
	}

	public float getTrimpScore() {
		return trimpScore;
	}

	public void setTrimpScore(float trimpScore) {
		this.trimpScore = trimpScore;
	}

	public float getDistance() {
		return distance;
	}

	public void setDistance(float distance) {
		this.distance = distance;
	}

	public float getSpeed() {
		return speed;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}
	
	public float getCalories() {
		return calories;
	}

	public void setCalories(float calories) {
		this.calories = calories;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getDuration() {
		return duration;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}

	public int getTemperature() {
		return temperature;
	}

	public void setTemperature(int temperature) {
		this.temperature = temperature;
	}

	public int getCondition() {
		return condition;
	}

	public void setCondition(int condition) {
		this.condition = condition;
	}

	public Vector<HeartRateData> getHeartRateTrace() {
		return heartRateTrace;
	}

	public void setHeartRateTrace(Vector<HeartRateData> heartRateTrace) {
		this.heartRateTrace = heartRateTrace;
	}
	
	
}
