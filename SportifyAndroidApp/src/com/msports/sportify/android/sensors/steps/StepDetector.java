package com.msports.sportify.android.sensors.steps;

import com.msports.sportify.android.sensors.speed.SpeedListener;

/**
 * Detects a step
 */
public class StepDetector
{
    private final static float   DEFAULT_DIFF_LIMIT = 3; //default sensitivity
    private float   m_PrevValue;
    private float   m_PrevDirection;
    private float   m_PrevExtreme[] = new float[2];
    private float   m_PrevDiff;
    private int     m_PrevMatch = -1;
    
    private StepListener mStepListener;
    private SpeedListener mSpeedListener;
    
    public StepDetector() {
    }
    
    public void setStepListener(StepListener _stepListener) {
        mStepListener = _stepListener;
    }
    
    public void setSpeedListener(SpeedListener _speedListener) {
        mSpeedListener = _speedListener;
    }
    
	public void onSensorChanged(float[] eventValues) {
		float actValue = (float)Math.sqrt(Math.pow(eventValues[0], 2)+Math.pow(eventValues[1], 2)+Math.pow(eventValues[2], 2));

		float direction = 0;
		
		if (actValue > m_PrevValue) {
			direction = 1;
		} else if (actValue < m_PrevValue) {
			direction = -1;
		}

		if (direction == -m_PrevDirection) { //check if direction changed
			// Direction had changed
			int extremeType = 1; //is it a minimum or a maximum?
			if (direction > 0) { 
				extremeType = 0;
			}
			
			m_PrevExtreme[extremeType] = m_PrevValue;
			float diff = Math.abs(m_PrevExtreme[extremeType] -  m_PrevExtreme[1 - extremeType]);

			if (diff > DEFAULT_DIFF_LIMIT) { //is threshold big enough

				boolean isNearlyAsLargeAsPrevious = diff > (m_PrevDiff * 2 / 3);
				boolean isPreviousLargeEnough = m_PrevDiff > (diff / 3);
				boolean isNotContra = (m_PrevMatch != 1 - extremeType);

				if (isNearlyAsLargeAsPrevious && isPreviousLargeEnough && isNotContra) {
					mStepListener.onStep();
					//mSpeedListener.onSpeedChanged();
					m_PrevMatch = extremeType;
				} else {
					m_PrevMatch = -1;
				}
			}
			m_PrevDiff = diff;
		}
		m_PrevDirection = direction;
		m_PrevValue = actValue;
	}
}