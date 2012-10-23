package com.msports.sportify.android.session;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.msports.sportify.android.MainActivity;
import com.msports.sportify.android.model.SessionModel;
import com.msports.sportify.android.sensors.SensorFactoryManager;
import com.msports.sportify.android.sensors.heartRate.HeartRateData;
import com.msports.sportify.android.sensors.heartRate.HeartRateListener;
import com.msports.sportify.android.sensors.steps.StepData;
import com.msports.sportify.android.sensors.steps.StepListener;
import com.msports.sportify.preferences.PedometerSettings;

public class SessionManager implements HeartRateListener, StepListener{

	public MainActivity mainActivity;
	
	private SessionModel model;
	
	private int sumHeartRate;
	private int countHeartRate;
	private long timestampPulse;
	
	public SessionManager(MainActivity mainActivity) {
		this.mainActivity = mainActivity;
		model = new SessionModel();
		SensorFactoryManager sensorFactoryManager = SensorFactoryManager.getInstance(mainActivity);
		sensorFactoryManager.registerSimulationHeartRateListener(this);
		sensorFactoryManager.registerStepListener(this);
		
		sumHeartRate = 0;
		countHeartRate = 0;
		timestampPulse = -1;
	}

	@Override
	synchronized public void onUpdateHeartRateSensorData(HeartRateData data) {
		int heartrate = data.getHeartRate();
		
		model.setCurrentHeartRate(heartrate);
		
		if (heartrate > model.getMaxHeartRate()) {
			model.setMaxHeartRate(heartrate);
		}
		
		int percent = (heartrate / model.getMaxHeartRate()) * 100;
		
		model.setCurrentPercentageOfMaxHeartRate(percent);
		
		if (model.getMinHeartRate() == -1) {
			model.setMinHeartRate(heartrate);
		} else if (heartrate < model.getMinHeartRate()) {
			model.setMinHeartRate(heartrate);
		}
		
		sumHeartRate += heartrate;
		countHeartRate++;
		
		model.setAvgHeartRate(sumHeartRate/countHeartRate);
		
		PedometerSettings settings = new PedometerSettings(mainActivity);
		
		int gender = settings.getGender();
		int age = settings.getAge();
		float hrmax = 208 - (0.7f * age);
		
		//calculate trimp
		if (timestampPulse != -1) {
			float diff = System.currentTimeMillis() - timestampPulse;
			diff = diff / 1000f / 60f; //minutes
			
			int hrrest = settings.getRestingHeartRate();
			
			float trimp = model.getTrimpScore();			
			
			float hrReserve = (model.getCurrentHeartRate()-hrrest)/(hrmax-hrrest);
			float y = hrReserve;
			
			if (gender == 0) {
				y *= 1.67f;
			} else {
				y *= 1.92f;
			}
			
			trimp += diff * hrReserve * 0.64f * Math.pow(Math.E, y);
			int rounding = (int)(trimp * 100);
			model.setTrimpScore(((float)rounding)/100f);
		}
		
		//calculate calories
		float weight = settings.getBodyWeight();
		float vomax = settings.getVoMax();
		
		float cal = -59.3954f + gender * (-36.3781f + 0.271f * age + 0.394f * weight +
				0.404f * vomax + 0.634f * heartrate) + (1-gender) *
				(0.274f * age + 0.103f * weight + 0.38f * vomax + 0.45f * heartrate);
		cal = cal / 1000; //kcal
		model.setCalories(model.getCalories() + cal);
		
		timestampPulse = System.currentTimeMillis();		
		
		mainActivity.updateViews(model);
	}

	@Override
	public void onUpdateStepSensor(StepData _data) {
		model.setSpeed(_data.getM_speed());
		model.setDistance(_data.getM_distance());
		mainActivity.updateViews(model);
	}	
}
