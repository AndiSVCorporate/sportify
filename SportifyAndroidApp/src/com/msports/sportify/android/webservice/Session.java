package com.msports.sportify.android.webservice;

public class Session {
	
	private long startTime;
	private long duration;
	
	private int temperature;
	private int condition;
	
	private int avgHeartRate;
	private int maxHeartRate;
	private String heartRateTrace;
	
	private int trimpScore;
	private int calories;
	
	private int distance;
	
	public Session() {
		this.startTime = System.currentTimeMillis();
		this.duration = 0;
		this.temperature = -1;
		this.condition = -1;
		this.avgHeartRate = -1;
		this.maxHeartRate = -1;
		this.heartRateTrace = null;
		this.trimpScore = -1;
		this.calories = -1;
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
	public int getCalories() {
		return calories;
	}
	public void setCalories(int calories) {
		this.calories = calories;
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


	public String getHeartRateTrace() {
		return heartRateTrace;
	}


	public void setHeartRateTrace(String heartRateTrace) {
		this.heartRateTrace = heartRateTrace;
	}


	public int getTrimpScore() {
		return trimpScore;
	}


	public void setTrimpScore(int trimpScore) {
		this.trimpScore = trimpScore;
	}


	public int getDistance() {
		return distance;
	}


	public void setDistance(int distance) {
		this.distance = distance;
	}


	public void setMaxHeartRate(int maxHeartRate) {
		this.maxHeartRate = maxHeartRate;
	}
	
	
}
