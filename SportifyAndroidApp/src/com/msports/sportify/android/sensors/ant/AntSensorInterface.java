package com.msports.sportify.android.sensors.ant;

/**
 * Interface for all Sensors which uses Ant for getting Data and setting up an ANT-Channel
 * 
 * @author Alexander
 *
 */
public interface AntSensorInterface {

	public ChannelConfiguration getChannelConfiguration();
	
	public void decodeAntMessage(byte[] message);	
	
}
