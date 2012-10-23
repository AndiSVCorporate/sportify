package com.msports.sportify.android.webservice;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


import android.util.Log;

/**
 * Simple client for REST services.
 * 
 * @author Luke Lowrey
 * @author Robert Schenkenfelder (edited)
 * 
 */
public class RestClient extends SportifyHttpClient<String> {

	public RestClient(String url) {
		super(url);
	}

	@Override
	protected String convertStream(InputStream is) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			Log.e(LOG_TAG, "RestClient error", e);
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				Log.e(LOG_TAG, "RestClient error", e);
			}
		}
		return sb.toString();
	}
}