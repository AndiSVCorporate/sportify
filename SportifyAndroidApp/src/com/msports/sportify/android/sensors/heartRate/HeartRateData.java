package com.msports.sportify.android.sensors.heartRate;

public class HeartRateData {

	private int heartRate;
	private long runtime;
	
	public HeartRateData(int heartRate) {
		this.heartRate = heartRate;
	}

	public int getHeartRate() {
		return heartRate;
	}

	public void setHeartRate(int heartRate) {
		this.heartRate = heartRate;
	}

	public long getRuntime() {
		return runtime;
	}

	public void setRuntime(long runtime) {
		this.runtime = runtime;
	}
	
	
	
}
