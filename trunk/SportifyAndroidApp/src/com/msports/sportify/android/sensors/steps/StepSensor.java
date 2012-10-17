package com.msports.sportify.android.sensors.steps;

import com.msports.sportify.preferences.PedometerSettings;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

/**
 * Detects a step
 */
public class StepSensor implements SensorEventListener 
{
	private Context mContext;
	
	//step detection variables
	private float   mLimit = 1.97f;
    private float   mLastValues[] = new float[3*2];
    private float   mScale[] = new float[2];
    private float   mYOffset;

    private float   mLastDirections[] = new float[3*2];
    private float   mLastExtremes[][] = { new float[3*2], new float[3*2] };
    private float   mLastDiff[] = new float[3*2];
    private int     mLastMatch = -1;
    
    private StepListener mStepListener;
    private SensorManager sensorManager;
	private Sensor accelerometer;
	private StepData m_stepData;
	
	//speed calculation variables
	private long timestampPrevStep = 0;
    
    public StepSensor(Context context) {
    	mContext = context;
    	
    	sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
    	accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    	sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    	m_stepData = new StepData();
    	
    	int h = 480; // TODO: remove this constant
        mYOffset = h * 0.5f;
        mScale[0] = - (h * 0.5f * (1.0f / (SensorManager.STANDARD_GRAVITY * 2)));
        mScale[1] = - (h * 0.5f * (1.0f / (SensorManager.MAGNETIC_FIELD_EARTH_MAX)));
    }
    
    public void setStepListener(StepListener _stepListener) {
        mStepListener = _stepListener;
    }  
    
    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor sensor = event.sensor; 
        synchronized (this) {
            if (sensor.getType() == Sensor.TYPE_ORIENTATION) {
            }
            else {
                int j = (sensor.getType() == Sensor.TYPE_ACCELEROMETER) ? 1 : 0;
                if (j == 1) {
                    float vSum = 0;
                    for (int i=0 ; i<3 ; i++) {
                        final float v = mYOffset + event.values[i] * mScale[j];
                        vSum += v;
                    }
                    int k = 0;
                    float v = vSum / 3;
                    
                    float direction = (v > mLastValues[k] ? 1 : (v < mLastValues[k] ? -1 : 0));
                    if (direction == - mLastDirections[k]) {
                        // Direction changed
                        int extType = (direction > 0 ? 0 : 1); // minumum or maximum?
                        mLastExtremes[extType][k] = mLastValues[k];
                        float diff = Math.abs(mLastExtremes[extType][k] - mLastExtremes[1 - extType][k]);

                        if (diff > mLimit) {
                            
                            boolean isAlmostAsLargeAsPrevious = diff > (mLastDiff[k]*2/3);
                            boolean isPreviousLargeEnough = mLastDiff[k] > (diff/3);
                            boolean isNotContra = (mLastMatch != 1 - extType);
                            
                            if (isAlmostAsLargeAsPrevious && isPreviousLargeEnough && isNotContra) {
                                calculateDistance();
                                calculateSpeed(System.currentTimeMillis());
                                mStepListener.onUpdateStepSensor(m_stepData);
                                mLastMatch = extType;
                            }
                            else {
                                mLastMatch = -1;
                            }
                        }
                        mLastDiff[k] = diff;
                    }
                    mLastDirections[k] = direction;
                    mLastValues[k] = v;
                }
            }
        }
    }
    
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
	}	
	
	public void calculateSpeed(long currentTimeMillis) {
		if (timestampPrevStep == 0) {
			timestampPrevStep = currentTimeMillis;
		}
		float diff = currentTimeMillis-timestampPrevStep;
		float timeHours = diff / 1000 / 3600;
		float stepLengthMeter = new PedometerSettings(mContext).getStepLength() / 100;
		float speed = stepLengthMeter/1000 / timeHours; //in km/h
		timestampPrevStep = currentTimeMillis;
		
		speed = (int)(speed*100)/100.0f;
		
		m_stepData.setM_speed(speed);
	}
	
	public void calculateDistance() {
		float stepLengthMeter = new PedometerSettings(mContext).getStepLength() / 100;
		m_stepData.setM_distance(m_stepData.getM_distance()+stepLengthMeter);
	}
}