package com.msports.sportify.android.sensors.heartRate;

import com.msports.sportify.android.sensors.Sensor;

public class SimulationHeartRateSensor extends Sensor{

	private HeartRateListener listener;
	private SimulationThread simulationThread;
	
	public SimulationHeartRateSensor(HeartRateListener listener) {
		this.listener = listener;
	}
	
	private class SimulationThread extends Thread {
				
		
		@Override
		public void run() {
			int heartRate;
			float random;
			
			while(true) {
				random = (float) (Math.random()*40);
				heartRate = 160 + (int)random;
				listener.onUpdateHeartRateSensorData(new HeartRateData(heartRate));		
				try {
					sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void connect() {
		simulationThread = new SimulationThread();
		simulationThread.start();
	}

	@Override
	public void disconnect() {
		simulationThread.stop();
	}
}
