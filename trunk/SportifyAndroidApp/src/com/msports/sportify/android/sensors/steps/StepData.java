package com.msports.sportify.android.sensors.steps;

public class StepData {
	
	public float m_speed; //km/h
	public float m_distance; //km
	
	public float getM_speed() {
		return m_speed;
	}

	public void setM_speed(float m_speed) {
		this.m_speed = m_speed;
	}

	public float getM_distance() {
		return m_distance;
	}

	public void setM_distance(float m_distance) {
		this.m_distance = m_distance;
	}
	
	public StepData() {
		m_speed = 0;
		m_distance = 0;
	}

}
