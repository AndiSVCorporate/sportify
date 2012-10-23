package com.msports.sportify.android.model;

public class SessionModel {

	private int avgHeartRate;
	private int minHeartRate;
	private int maxHeartRate;
	private int currentPercentageOfMaxHeartRate;
	private int currentHeartRate;
	
	private float distance; 
	private float speed;
	
	private int trimpScore;

	public SessionModel() {
		resetValues();
	}
	
	public void resetValues() {
		avgHeartRate = -1;
		minHeartRate = -1;
		maxHeartRate = -1;
		currentHeartRate = -1;
		currentPercentageOfMaxHeartRate = -1;
		trimpScore = -1;
		distance = -1;
		speed = -1;
	}
	
	public int getAvgHeartRate() {
		return avgHeartRate;
	}

	public void setAvgHeartRate(int avgHeartRate) {
		this.avgHeartRate = avgHeartRate;
	}

	public int getMinHeartRate() {
		return minHeartRate;
	}

	public void setMinHeartRate(int minHeartRate) {
		this.minHeartRate = minHeartRate;
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

	public int getTrimpScore() {
		return trimpScore;
	}

	public void setTrimpScore(int trimpScore) {
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
	
	
	
	
}
