package com.msports.sportify.shared;

import java.io.Serializable;

public class HeartRateData implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -146515644871885998L;
	
	private int heartRate;
	private long runtime;
	
	public HeartRateData(int heartRate) {
		this.heartRate = heartRate;
	}

	public HeartRateData() {
		// TODO Auto-generated constructor stub
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
