package com.msports.sportify.android.model;

import java.util.Date;
import android.content.SharedPreferences;

public class PedometerModel {
	
	public static final int RECOMMENDED_STEPS_PER_DAY = 4000;
	
	private int m_stepsToday;
	private int m_stepsToGo;
	private int m_totalSteps;
	private float m_stepLength;
	
	public PedometerModel(int _totalSteps, boolean _firstOpenedToday, int _stepsToday) {
		if (_firstOpenedToday) {
			m_stepsToday = 0;
		} else {
			m_stepsToday = _stepsToday;
		}
		
		m_totalSteps = _totalSteps;
		
		if (m_stepsToday >= RECOMMENDED_STEPS_PER_DAY) {
			m_stepsToGo = 0;
		} else {
			m_stepsToGo = RECOMMENDED_STEPS_PER_DAY - m_stepsToday;
		}		
	}
	
	public void incrementStep() {
		m_stepsToday++;
		m_totalSteps++;
		
		if (m_stepsToGo > 0) {
			m_stepsToGo--;
		}		
	}
	
	public String getStepsToday() {
		return String.valueOf(m_stepsToday);
	}
	
	public String getStepsToGo() {
		return String.valueOf(m_stepsToGo);
	}
	
	public String getTotalSteps() {
		return String.valueOf(m_totalSteps);
	}
	
	public void savePreferences(SharedPreferences settings) {
		SharedPreferences.Editor editor = settings.edit();
	    editor.putInt("totalSteps", m_totalSteps);	    
	    editor.putLong("prevOpening", new Date().getTime());
	    editor.putInt("stepsToday", m_stepsToday);

	    editor.commit();
	}
}
