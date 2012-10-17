package com.msports.sportify.android;

import java.util.Date;

import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.msports.sportify.android.model.PedometerModel;
import com.msports.sportify.android.sensors.SensorFactoryManager;
import com.msports.sportify.android.sensors.speed.SpeedSensor;
import com.msports.sportify.android.sensors.steps.StepData;
import com.msports.sportify.android.sensors.steps.StepListener;
import com.msports.sportify.android.sensors.steps.StepSensor;
import com.msports.sportify.preferences.PedometerSettings;

public class PedometerFragment extends Fragment implements StepListener {

	private View v;
	
	private PedometerModel m_model;
    
	private TextView m_totalStepsView;
	private TextView m_stepsToGoView;
	private TextView m_stepsTodayView;
	private TextView m_maxStepsPerDayView;
	private TextView m_minStepsPerDayView;
	
	private StepSensor m_stepDetector;
	private SpeedSensor m_speedSensor;
	
	public PedometerFragment(PedometerModel _model) {
		m_model = _model;	
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.pedometer, null);
		
		v = view;	
        
        //mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        //mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        //mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        
        m_totalStepsView = (TextView)view.findViewById(R.id.totalstepsview);
        m_stepsToGoView = (TextView)view.findViewById(R.id.stepstogoview);
        m_stepsTodayView = (TextView)view.findViewById(R.id.stepstodayview);
    	m_maxStepsPerDayView = (TextView)view.findViewById(R.id.maxstepsperdayview);
    	m_minStepsPerDayView = (TextView)view.findViewById(R.id.minstepsperdayview);
        
        setCurrentValues();        
        
    	SensorFactoryManager.getInstance(getActivity()).registerStepListener(this);
        
		return view;
	}
	
	public void setCurrentValues() {
		m_totalStepsView.setText(m_model.getTotalSteps());
		m_stepsToGoView.setText(m_model.getStepsToGo());
		m_stepsTodayView.setText(m_model.getStepsToday());
	}

	public void onUpdateStepSensor(StepData _stepData) {
		Log.i("pedometer", "step done");
		m_model.incrementStep();
		setCurrentValues();
	}

	
}
