package com.msports.sportify.android.session;

import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.test.IsolatedContext;
import android.util.Log;

import com.msports.sportify.android.MainActivity;
import com.msports.sportify.android.model.SessionModel;
import com.msports.sportify.android.sensors.SensorFactoryManager;
import com.msports.sportify.android.sensors.heartRate.HeartRateData;
import com.msports.sportify.android.sensors.heartRate.HeartRateListener;
import com.msports.sportify.android.sensors.steps.StepData;
import com.msports.sportify.android.sensors.steps.StepListener;
import com.msports.sportify.android.webservice.SportifyWebService;
import com.msports.sportify.preferences.PedometerSettings;

public class SessionManager implements HeartRateListener, StepListener {

	public MainActivity mainActivity;

	private SessionModel model;

	private boolean sessionRunning;

	private int sumHeartRate;
	private int countHeartRate;
	private long timestampPulse;

	private Handler timeHandler;

	public SessionManager(MainActivity mainActivity) {
		this.mainActivity = mainActivity;
		model = SessionModel.getInstance(this);
		SensorFactoryManager sensorFactoryManager = SensorFactoryManager
				.getInstance(mainActivity);
		sensorFactoryManager.registerAntHeartRateListener(this);
		sensorFactoryManager.registerStepListener(this);

		sumHeartRate = 0;
		countHeartRate = 0;
		timestampPulse = -1;

		sessionRunning = false;

		timeHandler = new Handler();
	}

	public void startSession() {
		model.resetValues();
		model.startTime.set(System.currentTimeMillis());
		sessionRunning = true;
		timeHandler = new Handler();
		timeHandler.postDelayed(new Runnable() {

			@Override
			public void run() {
				if (sessionRunning) {
					model.duration.set(System.currentTimeMillis()
							- model.startTime.get());
					SessionManager.this.mainActivity.updateViews(model);
					if (timeHandler != null) {
						timeHandler.postDelayed(this, 1000);
					}
				}
			}
		}, 1000);
	}

	public void stopSession() {
		sessionRunning = false;
		timeHandler = null;
		model.duration.set(System.currentTimeMillis() - model.startTime.get());
		SportifyWebService.sendSessionStoringRequest(model);
		model.resetValues();
		mainActivity.updateViews(model);
	}

	@Override
	synchronized public void onUpdateHeartRateSensorData(
			final HeartRateData data) {

		mainActivity.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				int heartrate = data.getHeartRate();
				Log.i("HR:", "hr: " + heartrate);
				data.setRuntime(System.currentTimeMillis() - model.startTime.get());
				model.heartRateTrace.add(data);
				model.currentHeartRate.set(heartrate);
				if (sessionRunning) {

					if (heartrate > model.maxHeartRate.get()) {
						model.maxHeartRate.set(heartrate);
					}

					int percent = (heartrate / model.maxHeartRate.get()) * 100;

					model.currentPercentageOfMaxHeartRate.set(percent);

					sumHeartRate += heartrate;
					countHeartRate++;

					model.avgHeartRate.set(sumHeartRate / countHeartRate);

					PedometerSettings settings = new PedometerSettings(
							mainActivity);

					int gender = settings.getGender();
					int age = settings.getAge();
					float hrmax = 208 - (0.7f * age);

					// calculate trimp
					if (timestampPulse != -1) {
						float diff = System.currentTimeMillis()
								- timestampPulse;
						diff = diff / 1000f / 60f; // minutes

						int hrrest = settings.getRestingHeartRate();

						float trimp = model.trimpScore.get();

						float hrReserve = (model.currentHeartRate.get() - hrrest)
								/ (hrmax - hrrest);
						float y = hrReserve;

						if (gender == 0) {
							y *= 1.67f;
						} else {
							y *= 1.92f;
						}

						trimp += diff * hrReserve * 0.64f * Math.pow(Math.E, y);
						int rounding = (int) (trimp * 100);
						model.trimpScore.set(((float) rounding) / 100f);
					}

			// calculate calories
			float weight = settings.getBodyWeight();
			int par = settings.getPyhsicalActRating();
			float height = settings.getBodyHeight();
			
			float vomax = (0.133f * age) - (0.005f * (float)Math.pow(age, 2)) +
			(11.403f * gender) + (1.463f * par) +
			(9.17f * height/100) - (0.254f * weight) + 34.143f;

			float cal = -59.3954f
					+ gender
					* (-36.3781f + 0.271f * age + 0.394f * weight + 0.404f
							* vomax + 0.634f * heartrate)
					+ (1 - gender)
					* (0.274f * age + 0.103f * weight + 0.38f * vomax + 0.45f * heartrate);
			cal = cal / 4.168f / 60; //converted to kcal/second
			model.calories.set(model.calories.get() + cal);

					timestampPulse = System.currentTimeMillis();
				}

			}
		});
	}

	

	@Override
	public void onUpdateStepSensor(StepData _data) {
		model.speed.set(_data.getM_speed());
		model.distance.set(_data.getM_distance());
		mainActivity.updateViews(model);
	}
}
