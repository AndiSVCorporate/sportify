package com.msports.sportify.client;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;


public class Session implements Serializable{

	private int stepsToday;
	private int avgStepFrequency;
	private int maxStepFrequency;
	private long time;
	private int temperature;
	private int condition;
	private int calories;
	private int avgHeartRate;
	private int maxHeartRate;
	private String date;	
	
	public Session() {
		this.stepsToday = 0;
		this.time = 0;
		this.temperature = 0;
	}
	
	public Session(int stepsToday,  long time, int temperature) {
		this.stepsToday = stepsToday;
		this.time = time;
		this.temperature = temperature;
	}
	
	public int getStepsToday() {
		return stepsToday;
	}
	public void setStepsToday(int stepsToday) {
		this.stepsToday = stepsToday;
	}
	public int getAvgStepFrequency() {
		return avgStepFrequency;
	}
	public void setAvgStepFrequency(int avgStepFrequency) {
		this.avgStepFrequency = avgStepFrequency;
	}
	public int getMaxStepFrequency() {
		return maxStepFrequency;
	}
	public void setMaxStepFrequency(int maxStepFrequency) {
		this.maxStepFrequency = maxStepFrequency;
	}
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
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
	public void setMaxHeartRate(int maxHeartRate) {
		this.maxHeartRate = maxHeartRate;
	}
	
	public void setDateString(String s) {		
	    this.date = s;		
	}
	
	public String getDateString() {    
		return this.date;
	}
	
	
}
