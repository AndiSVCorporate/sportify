package com.msports.sportify.server;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Vector;

import com.msports.sportify.shared.HeartRateData;



public class WSUtils {
	
	public final static String TAG = "WSUtils";

	public static String encodeHeartRateTraceToString(List<HeartRateData> trace) {
		return base64Encode(encodeHeartRateTrace(trace));
	}
	
	public static List<HeartRateData> decodeStringToHeartRateTrace(String base64Data) throws UnsupportedEncodingException, IOException {
		return decodeHeartRateTrace(base64Decode(base64Data.getBytes(Base64.PREFERRED_ENCODING)));
	}
	
	/**
	 * encodes the heartRateTrace to binary format (byte-array)
	 * 
	 * @param trace
	 *            the given trace (as list) to get the byteArray from
	 * @return a byteArray representing the heartRateTrace
	 */
	public static byte[] encodeHeartRateTrace(List<HeartRateData> trace) {
		if (trace == null || trace.size() == 0) {
			return new byte[0];
		}
		DataOutputStream os = null;
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			os = new DataOutputStream(bos);
			os.writeInt(trace.size());
			for (HeartRateData data : trace) {
				os.writeLong(data.getRuntime());
				os.writeByte(data.getHeartRate());
			}
			return bos.toByteArray();
		} catch (Exception e) {
			
			return new byte[0];
		}
	}
	

	/**
	 * decodes the heartRateTrace from binary format to a list of heartRateData's NOTE: the gpsTrace and heartRateTrace
	 * are stored as binary format in the database
	 * 
	 * @param b
	 *            the byteArray representing the heartRateTrace
	 * @return the heartRateTrace (a list of heartRateData's)
	 */
	public static List<HeartRateData> decodeHeartRateTrace(byte[] b) {
		List<HeartRateData> trace = new Vector<HeartRateData>();

		if (b == null || b.length == 0) {
			return trace;
		}

		HeartRateData data = null;
		try {
			ByteArrayInputStream bis = new ByteArrayInputStream(b);
			DataInputStream is = new DataInputStream(bis);
			int num = is.readInt();

			for (int i = 0; i < num; i++) {
				data = new HeartRateData();				
				data.setRuntime(is.readLong());
				data.setHeartRate(is.readByte() & 0xFF);
				trace.add(data);
			}
		} catch (Exception e) {
		}
		return trace;
	}
	
	public static String base64Encode(byte[] bytes) {
		if (bytes == null || bytes.length == 0) {
		    return "";
		}
		return Base64.encodeBytes(bytes, 0, bytes.length);
	}

	public static byte[] base64Decode(byte[] bytes) throws IOException {
		if (bytes == null || bytes.length == 0) {
		    return new byte[]{};
		}
		return Base64.decode(bytes);

	}

}
