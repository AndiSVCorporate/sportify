package com.msports.sportify.shared;

import java.io.Serializable;

public class DailyStepsData implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2676624179114736970L;
	
	private int stepsToday;
	private long date;
	
	public DailyStepsData() {
		stepsToday = -1;
		date = -1;
	}
	
	public DailyStepsData(int steps, long date) {
		this.stepsToday = steps;
		this.date = date;
	}
	
	public int getStepsToday() {
		return stepsToday;
	}
	public void setStepsToday(int stepsToday) {
		this.stepsToday = stepsToday;
	}
	public long getDate() {
		return date;
	}
	public void setDate(long date) {
		this.date = date;
	}	
}
