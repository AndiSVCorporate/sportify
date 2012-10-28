package com.msports.sportify.android.sensors.ant;


/**
 * Heart rate monitor channel configuration.
 *
 * @author Alexander Lenz
 */
public class HeartRateChannelConfiguration extends ChannelConfiguration {

  private static final int DEVICE_ID_KEY = 1;
  private static final byte DEVICE_TYPE = 0x78;
  private static final short MESSAGE_PERIOD = 8070;

  @Override
  public int getDeviceIdKey() {
    return DEVICE_ID_KEY;
  }

  @Override
  public byte getDeviceType() {
    return DEVICE_TYPE;
  }

  @Override
  public short getMessagPeriod() {
    return MESSAGE_PERIOD;
  }

}
