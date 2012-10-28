package com.msports.sportify.android.sensors.heartRate;

import android.content.Context;
import android.util.Log;

import com.msports.sportify.android.sensors.Sensor;
import com.msports.sportify.android.sensors.ant.AntManager;
import com.msports.sportify.android.sensors.ant.AntSensorInterface;
import com.msports.sportify.android.sensors.ant.ChannelConfiguration;
import com.msports.sportify.android.sensors.ant.HeartRateChannelConfiguration;

public class AntHeartRateSensor extends Sensor implements AntSensorInterface {

	private HeartRateChannelConfiguration channelConfig;
	private Context context;
	private HeartRateListener listener;
	private long lastSensorValueTimestamp;
	
	private static final long SENSOR_VALUE_INTERVAL = 1000l;
	
	private static final String TAG = "AntHeartRateSensor";

	public AntHeartRateSensor(Context context, HeartRateListener listener) {
		channelConfig = new HeartRateChannelConfiguration();
		this.context = context;
		this.listener = listener;
		lastSensorValueTimestamp = -1;
	}
	
	@Override
	public ChannelConfiguration getChannelConfiguration() {
		return channelConfig;
	}

	@Override
	public void decodeAntMessage(byte[] message) {
		long timestamp = System.currentTimeMillis();
		if((timestamp - lastSensorValueTimestamp) > SENSOR_VALUE_INTERVAL) {
			int heartRate = message[10] & 0xFF;
			HeartRateData newHrData = new HeartRateData(heartRate);
			listener.onUpdateHeartRateSensorData(newHrData);			
			lastSensorValueTimestamp = timestamp;
		}
	}

	@Override
	public void connect() {
		Log.e(TAG, "connected - registering to AntManager");
		//register on ANT-Manager
		AntManager.getInstance(context).registerAntSensor(this);
	}

	@Override
	public void disconnect() {
		Log.e(TAG, "disconnected - unregistering from AntManager");
		//unregister on ANT-Manager
		AntManager.getInstance(context).unregisterAntSensor(this);
	}

}
