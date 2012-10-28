package com.msports.sportify.android.sensors.ant;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.dsi.ant.AntDefine;
import com.dsi.ant.AntInterface;
import com.dsi.ant.AntInterfaceIntent;
import com.dsi.ant.AntMesg;
import com.dsi.ant.exception.AntInterfaceException;
import com.dsi.ant.exception.AntServiceNotConnectedException;

public class AntManager {

	private static AntManager instance;

//	private Vector<AntSensorInterface> antSensors;
	private Map<Byte, AntSensorInterface> antSensorMap;
	
	private AntManager(Context context) {
		this.context = context;
		
		antSensorMap = new HashMap<Byte, AntSensorInterface>();

		statusIntentFilter = new IntentFilter();
		statusIntentFilter.addAction(AntInterfaceIntent.ANT_ENABLED_ACTION);
		statusIntentFilter.addAction(AntInterfaceIntent.ANT_ENABLING_ACTION);
		statusIntentFilter.addAction(AntInterfaceIntent.ANT_DISABLED_ACTION);
		statusIntentFilter.addAction(AntInterfaceIntent.ANT_DISABLING_ACTION);
		statusIntentFilter.addAction(AntInterfaceIntent.ANT_RESET_ACTION);
		statusIntentFilter.addAction(AntInterfaceIntent.ANT_INTERFACE_CLAIMED_ACTION);
		statusIntentFilter.addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED);

		antInterface = new AntInterface();
		deviceId = Integer.MAX_VALUE;
	}

	public static AntManager getInstance(Context context) {
		if (instance == null) {
			instance = new AntManager(context);
		}
		return instance;
	}

	/**
	 * 
	 * @param antSensor
	 */
	public void registerAntSensor(AntSensorInterface antSensor) {
		boolean wasEmpty = antSensorMap.isEmpty();
		
		
		int channel = antSensorMap.size();
		if (!antSensorMap.containsValue(antSensor)) {
			antSensorMap.put((byte)channel,antSensor);
			antSensor.getChannelConfiguration().setAssignedAntChannel(channel);
			if (wasEmpty) {
				connect();
			} else {
				openChannel((byte) channel);
			}
//			openChannel((byte) channel);
			Log.e(TAG, "Registered an AntSensor to the AntManager on Channel: " + channel);
		} else {
			Log.e(TAG, "AntManager: AntSensor already connected to the AntManager on Channel: " + channel);
		}
	}

	/**
	 * 
	 * @param antSensor
	 */
	public void unregisterAntSensor(AntSensorInterface antSensor) {
		int channel = antSensor.getChannelConfiguration().getAssignedAntChannel();	
		try {
			context.unregisterReceiver(dataReceiver);
			if(channel != ChannelConfiguration.UNASSIGNED_ANT_CHANNEL) {
				closeChannel((byte) channel);				
			}
		} catch (IllegalArgumentException e) {
			// Can safely ignore
		}
		antSensorMap.remove((byte)channel);
		Log.e(TAG, "UnRegistered an AntSensor to the AntManager!!!");
		
		if (antSensorMap.isEmpty()) {
			disconnect();
		}
	}

	public enum ChannelStates {
		CLOSED, // Channel is closed
		PENDING_OPEN, // User has requested opening the channel, waiting for a
						// reset
		SEARCHING, // Channel is opened, but has not received data
		TRACKING_STATUS, // Channel is opened and has received status data
							// recently
		TRACKING_DATA, // Channel is opened and has received measurement data
						// recently
		OFFLINE // Channel is closed as the result of a search timeout
	}

	public static final short WILDCARD = 0;

	private static final String TAG = "runtastic ANT Sensor";
	private static final int CHANNELS = 1;
	private static final String RADIO_ANT = "ant";
	private static final byte ANT_NETWORK = (byte) 0x01;

	private final Context context;
	// private final com.runtastic.android.sensor.ant.ChannelConfiguration
	// channelConfig[];
	private final IntentFilter statusIntentFilter;
	private final AntInterface antInterface;

	private boolean serviceConnected = false;
	private boolean hasClaimedInterface = false;

//	// private SensorDataSet sensorDataSet = null;
//	private long lastSensorDataSetTime = 0;
//	private RawHeartRateData antSensorValue = new RawHeartRateData();

	private boolean requestedReset = false;

	private int deviceId;

	private AntInterface.ServiceListener serviceListener = new AntInterface.ServiceListener() {
		@Override
		public void onServiceConnected() {
			// setSensorState(Sensor.SensorState.CONNECTING);
			serviceConnected = true;
			try {
				hasClaimedInterface = antInterface.hasClaimedInterface();
				if (!hasClaimedInterface) {
					hasClaimedInterface = antInterface.claimInterface();
				}
				if (!hasClaimedInterface) {
					tryClaimAnt();
					return;
				}
				if (!antInterface.isEnabled()) {
					antInterface.enable();
				}
				requestReset();
				
				enableDataMessage(true);
				
			} catch (AntInterfaceException e) {
				handleAntError();
			}
		}

		@Override
		public void onServiceDisconnected() {
			Log.e(TAG, "AntInterface.ServiceListener::onServiceDisconnected");
			serviceConnected = false;
			if (hasClaimedInterface) {
				disableDataMessage();
			}
			// setSensorState(SensorState.DISCONNECTED);
		}
	};

	public void connect() {
		// disconnect();
		if (AntInterface.hasAntSupport(context)) {
			context.registerReceiver(statusReceiver, statusIntentFilter);
			enableDataMessage(true);
			if (!antInterface.initService(context, serviceListener)) {
				AntInterface.goToMarket(context);
			}
		}

	}

	public void disconnect() {
		try {
			context.unregisterReceiver(statusReceiver);
		} catch (IllegalArgumentException e) {
			// Can safely ignore
		}
		enableDataMessage(false);
		Log.e(TAG, "disconnect called , unregistered Receivers");
		if (serviceConnected) {
			try {
				if (hasClaimedInterface) {
					antInterface.releaseInterface();
				}
				hasClaimedInterface = false;
				antInterface.stopRequestForceClaimInterface();
			} catch (AntServiceNotConnectedException e) {
				// Can safely ignore
			} catch (AntInterfaceException e) {
				Log.w(TAG, "Exception in tearDonwChannel.", e);
			}
			antInterface.releaseService();
			serviceConnected = false;
		}
		// setSensorState(SensorState.DISCONNECTED);
	}

	// @Override
	// public SensorDataSet getSensorDataSet() {
	// return sensorDataSet;
	// }

	/**
	 * Tries to claim the ant interface.
	 */
	private void tryClaimAnt() {
		try {
			antInterface.requestForceClaimInterface("sportify");
		} catch (AntInterfaceException e) {
			handleAntError();
		}
	}

	/**
	 * Configures ant radio.
	 */
	private void configureAntRadio() {
		try {
			if (serviceConnected && hasClaimedInterface && antInterface.isEnabled()) {
				try {
					antInterface.ANTDisableEventBuffering();
				} catch (AntInterfaceException e) {
					Log.e(TAG, "Cannot disable event buffering.", e);
				}
			} else {
				Log.i(TAG, "Cannot disable event buffering now.");
			}
		} catch (AntInterfaceException e) {
			Log.e(TAG, "Unable to check enabled state.", e);
		}
	}

	/**
	 * Handles ant error.
	 */
	private void handleAntError() {
		clearAllChannels();
	}

	/**
	 * Opens a channel.
	 * 
	 * @param channel
	 *            the channel
	 */
	private void openChannel(byte channel) {
		// short deviceNumber = (short) PreferencesUtils.getInt(
		// context, channelConfig[channel].getDeviceIdKey(), WILDCARD);
		short deviceNumber = WILDCARD;
		antSensorMap.get(channel).getChannelConfiguration().setDeviceNumber(deviceNumber);
		antSensorMap.get(channel).getChannelConfiguration().setChannelState(ChannelStates.PENDING_OPEN);
		setupAntChannel(ANT_NETWORK, channel);
	}

	/**
	 * Closes a channel.
	 * 
	 * @param channel
	 *            the channel
	 */
	private void closeChannel(byte channel) {
		antSensorMap.get(channel).getChannelConfiguration().setInitializing(false);
		antSensorMap.get(channel).getChannelConfiguration().setDeinitializing(true);
		antSensorMap.get(channel).getChannelConfiguration().setChannelState(ChannelStates.CLOSED);
		try {
			antInterface.ANTCloseChannel(channel);
			// Note, unassign channel after getting channel closed event
		} catch (AntInterfaceException e) {
			Log.w(TAG, "Unable to close channel: " + channel, e);
			handleAntError();
		}
	}

	/**
	 * Clears all channels.
	 */
	private void clearAllChannels() {
		for (byte i = 0; i < antSensorMap.size(); i++) {
			antSensorMap.get(i).getChannelConfiguration().setChannelState(ChannelStates.CLOSED);
		}
		// setSensorState(SensorState.DISCONNECTED);
	}

	/**
	 * Requests reset.
	 */
	private void requestReset() {
		try {
			requestedReset = true;
			antInterface.ANTResetSystem();
			configureAntRadio();
		} catch (AntInterfaceException e) {
			Log.e(TAG, "Unable to reset ant.", e);
			requestedReset = false;
		}
	}

	// @Override
	public boolean isEnabled() {
		if (antInterface == null || !antInterface.isServiceConnected()) {
			return false;
		}
		try {
			return antInterface.isEnabled();
		} catch (AntInterfaceException e) {
			Log.w(TAG, "Unable to check enabled.", e);
			return false;
		}
	}

	private final BroadcastReceiver statusReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context c, Intent intent) {
			String ANTAction = intent.getAction();
			if (ANTAction.equals(AntInterfaceIntent.ANT_DISABLED_ACTION)) {
				clearAllChannels();
			} else if (ANTAction.equals(AntInterfaceIntent.ANT_RESET_ACTION)) {
				if (!requestedReset) {
					// Someone else triggered an ant reset
					clearAllChannels();
				} else {
					requestedReset = false;
					configureAntRadio();
				}
			} else if (ANTAction.equals(AntInterfaceIntent.ANT_INTERFACE_CLAIMED_ACTION)) {
				boolean wasClaimed = hasClaimedInterface;

				// Could also read ANT_INTERFACE_CLAIMED_PID from intent and see
				// if it
				// matches the current process PID.
				try {
					hasClaimedInterface = antInterface.hasClaimedInterface();
					if (hasClaimedInterface) {
						enableDataMessage(true);
					} else {
						if (wasClaimed) {
							// Claimed by another application
							disableDataMessage();
							// setSensorState(SensorState.DISCONNECTED);
						}
					}
				} catch (AntInterfaceException e) {
					handleAntError();
				}
			} else if (ANTAction.equals(Intent.ACTION_AIRPLANE_MODE_CHANGED)) {
				if (isAirPlaneMode()) {
					clearAllChannels();
				}
			}
		}
	};

	private final BroadcastReceiver dataReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context c, Intent intent) {
			if (intent.getAction().equals(AntInterfaceIntent.ANT_RX_MESSAGE_ACTION) && (antSensorMap.size() > 0)) {
				byte[] antRxMessage = intent.getByteArrayExtra(AntInterfaceIntent.ANT_MESSAGE);
				byte channel;
				switch (antRxMessage[AntMesg.MESG_ID_OFFSET]) {
				case AntMesg.MESG_BROADCAST_DATA_ID:
				case AntMesg.MESG_ACKNOWLEDGED_DATA_ID:
					channel = antRxMessage[AntMesg.MESG_DATA_OFFSET];
					if (antSensorMap.get(channel).getChannelConfiguration().getChannelState() != ChannelStates.CLOSED) {
						antSensorMap.get(channel).getChannelConfiguration().setChannelState(ChannelStates.TRACKING_DATA);
					}

					if (antSensorMap.get(channel).getChannelConfiguration().getDeviceNumber() == WILDCARD) {
						try {
							antInterface.ANTRequestMessage(channel, AntMesg.MESG_CHANNEL_ID_ID);
						} catch (AntInterfaceException e) {
							handleAntError();
						}
					}
					//decode AntMessage from the Ant-Sensor through the registered Sensor-Implementation and publish it
					antSensorMap.get(channel).decodeAntMessage(antRxMessage);
					break;
				case AntMesg.MESG_RESPONSE_EVENT_ID:
					handleResponseEventMessage(antRxMessage);
					break;
				case AntMesg.MESG_CHANNEL_ID_ID:
					channel = antRxMessage[AntMesg.MESG_DATA_OFFSET];
					short deviceNumber = (short) ((antRxMessage[AntMesg.MESG_DATA_OFFSET + 1] & 0xFF | ((antRxMessage[AntMesg.MESG_DATA_OFFSET + 2] & 0xFF) << 8)) & 0xFFFF);
					antSensorMap.get(channel).getChannelConfiguration().setDeviceNumber(deviceNumber);
					// PreferencesUtils.setInt(context,
					// channelConfig[channel].getDeviceIdKey(), deviceNumber);
					Toast.makeText(context, "ANT-HR-Sensor connected", Toast.LENGTH_SHORT).show();
					// setSensorState(SensorState.CONNECTED);
					break;
				default:
					break;
				}
			}
		}

		/**
		 * Handles response event message.
		 * 
		 * @param message
		 *            the message
		 */
		private void handleResponseEventMessage(byte[] message) {
			// For a list of possible message codes see ANT Message Protocol and
			// Usage
			// section 9.5.6.1 available from thisisant.com
			byte channel = message[AntMesg.MESG_DATA_OFFSET];
			if ((message[AntMesg.MESG_DATA_OFFSET + 1] == AntMesg.MESG_EVENT_ID)
					&& (message[AntMesg.MESG_DATA_OFFSET + 2] == AntDefine.EVENT_RX_SEARCH_TIMEOUT)) {
				// A channel timed out searching, unassign it
				antSensorMap.get(channel).getChannelConfiguration().setInitializing(false);
				antSensorMap.get(channel).getChannelConfiguration().setDeinitializing(false);
				antSensorMap.get(channel).getChannelConfiguration().setChannelState(ChannelStates.OFFLINE);
				try {
					antInterface.ANTUnassignChannel(channel);
				} catch (AntInterfaceException e) {
					handleAntError();
				}
				// setSensorState(SensorState.DISCONNECTED);
			}

			if (antSensorMap.get(channel).getChannelConfiguration().isInitializing()) {
				if (message[AntMesg.MESG_DATA_OFFSET + 2] != 0) {
					// Error response
					Log.e(TAG, String.format("Error code(%#02x) on message ID(%#02x) on channel %d",
							message[AntMesg.MESG_DATA_OFFSET + 2], message[AntMesg.MESG_DATA_OFFSET + 1], channel));
				} else {
					// Switch on message id
					switch (message[AntMesg.MESG_DATA_OFFSET + 1]) {
					case AntMesg.MESG_ASSIGN_CHANNEL_ID:
						try {
							antInterface.ANTSetChannelId(channel, antSensorMap.get(channel).getChannelConfiguration().getDeviceNumber(),
									antSensorMap.get(channel).getChannelConfiguration().getDeviceType(), ChannelConfiguration.TRANSMISSION_TYPE);
						} catch (AntInterfaceException e) {
							handleAntError();
						}
						break;
					case AntMesg.MESG_CHANNEL_ID_ID:
						try {
							antInterface.ANTSetChannelPeriod(channel, antSensorMap.get(channel).getChannelConfiguration().getMessagPeriod());
						} catch (AntInterfaceException e) {
							handleAntError();
						}
						break;
					case AntMesg.MESG_CHANNEL_MESG_PERIOD_ID:
						try {
							antInterface.ANTSetChannelRFFreq(channel, ChannelConfiguration.FREQUENCY);
						} catch (AntInterfaceException e) {
							handleAntError();
						}
						break;
					case AntMesg.MESG_CHANNEL_RADIO_FREQ_ID:
						try {
							// Disable high priority search
							antInterface.ANTSetChannelSearchTimeout(channel, (byte) 0);
						} catch (AntInterfaceException e) {
							handleAntError();
						}
						break;
					case AntMesg.MESG_CHANNEL_SEARCH_TIMEOUT_ID:
						try {
							// Set search timeout to 30 seconds (low priority
							// search)
							antInterface.ANTSetLowPriorityChannelSearchTimeout(channel, (byte) 12);
						} catch (AntInterfaceException e) {
							handleAntError();
						}
						break;
					case AntMesg.MESG_SET_LP_SEARCH_TIMEOUT_ID:
						if (antSensorMap.get(channel).getChannelConfiguration().getDeviceNumber() == WILDCARD) {
							try {
								// Configure proximity search, if using wild
								// card search
								antInterface.ANTSetProximitySearch(channel, ChannelConfiguration.PROXIMITY_SEARCH);
							} catch (AntInterfaceException e) {
								handleAntError();
							}
						} else {
							try {
								antInterface.ANTOpenChannel(channel);
							} catch (AntInterfaceException e) {
								handleAntError();
							}
						}
						break;
					case AntMesg.MESG_PROX_SEARCH_CONFIG_ID:
						try {
							antInterface.ANTOpenChannel(channel);
						} catch (AntInterfaceException e) {
							handleAntError();
						}
						break;
					case AntMesg.MESG_OPEN_CHANNEL_ID:
						antSensorMap.get(channel).getChannelConfiguration().setInitializing(false);
						antSensorMap.get(channel).getChannelConfiguration().setChannelState(ChannelStates.SEARCHING);
						break;
					default:
						break;
					}
				}
			} else if (antSensorMap.get(channel).getChannelConfiguration().isDeinitializing()) {
				if ((message[AntMesg.MESG_DATA_OFFSET + 1] == AntMesg.MESG_EVENT_ID)
						&& (message[AntMesg.MESG_DATA_OFFSET + 2] == AntDefine.EVENT_CHANNEL_CLOSED)) {
					try {
						antInterface.ANTUnassignChannel(channel);
					} catch (AntInterfaceException e) {
						handleAntError();
					}
				} else if ((message[AntMesg.MESG_DATA_OFFSET + 1] == AntMesg.MESG_UNASSIGN_CHANNEL_ID)
						&& (message[AntMesg.MESG_DATA_OFFSET + 2] == AntDefine.RESPONSE_NO_ERROR)) {
					antSensorMap.get(channel).getChannelConfiguration().setDeinitializing(false);
				}
			}
		}
	};

	/**
	 * Sets up ant channel.
	 * 
	 * @param networkNumber
	 *            the network number
	 * @param channel
	 *            the channel
	 */
	private void setupAntChannel(byte networkNumber, byte channel) {
		try {
			antSensorMap.get(channel).getChannelConfiguration().setInitializing(true);
			antSensorMap.get(channel).getChannelConfiguration().setDeinitializing(false);

			// Assign as slave channel on selected network
			antInterface.ANTAssignChannel(channel, AntDefine.PARAMETER_RX_NOT_TX, networkNumber);

			// The rest of the channel configuration will occur after the
			// response is
			// received in handleResponseEventMessage
		} catch (AntInterfaceException aie) {
			handleAntError();
		}
	}

	/**
	 * Enables data message.
	 * 
	 * @param enabled
	 *            true to enable
	 */
	private void enableDataMessage(boolean enabled) {
		if (enabled) {
			context.registerReceiver(dataReceiver, new IntentFilter(AntInterfaceIntent.ANT_RX_MESSAGE_ACTION));
			for (int i = 0; i < antSensorMap.size(); i++) {
				openChannel((byte) i);
			}
		} else {
			try {
				context.unregisterReceiver(dataReceiver);
				for (int i = 0; i < antSensorMap.size(); i++) {
					closeChannel((byte) i);
				}
			} catch (IllegalArgumentException e) {
				// Can safely ignore
			}
		}
	}
	
	private void disableDataMessage() {
		try {
			context.unregisterReceiver(dataReceiver);
			for (int i = 0; i < CHANNELS; i++) {
				closeChannel((byte) i);
			}
		} catch (IllegalArgumentException e) {
			// Can safely ignore
		}
	}
	
	

	/**
	 * Returns true if in airplane mode.
	 */
	private boolean isAirPlaneMode() {
		if (!Settings.System.getString(context.getContentResolver(), Settings.System.AIRPLANE_MODE_RADIOS).contains(
				RADIO_ANT)) {
			return false;
		}
		if (Settings.System.getInt(context.getContentResolver(), Settings.System.AIRPLANE_MODE_ON, 0) == 0) {
			return false;
		}

		try {
			Field field = Settings.System.class.getField("AIRPLANE_MODE_TOGGLEABLE_RADIOS");
			return !Settings.System.getString(context.getContentResolver(), (String) field.get(null)).contains(
					RADIO_ANT);
		} catch (Exception e) {
			// This is expected if the list does not yet exist, so just return
			// true
			return true;
		}
	}
}
