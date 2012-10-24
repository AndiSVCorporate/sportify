package com.msports.sportify.android.webservice;

import android.util.Log;

import com.google.gson.Gson;
import com.msports.sportify.android.model.SessionModel;
import com.msports.sportify.android.webservice.SportifyHttpClient.RequestMethod;

public class SportifyWebService {

	public static final String STORE_SESSION_URL = "http://sportify-msports.appspot.com/storeSession";
	public static final String PARAM_DATA = "data";

	public static void sendSessionStoringRequest(SessionModel model) {
		RestClient client = new RestClient(STORE_SESSION_URL);
		Session session = new Session();
		session.setStartTime(model.getStartTime());
		session.setDuration(model.getDuration());
		session.setAvgHeartRate(model.getAvgHeartRate());
		session.setCalories((int) model.getCalories());
		session.setDistance((int) model.getDistance());
		session.setMaxHeartRate(model.getMaxHeartRate());
		session.setTemperature(model.getTemperature());
		session.setTrimpScore((int) model.getTrimpScore());

		Gson gson = new Gson();
		String json = gson.toJson(session);
		String body = json.toString();
		Log.i("JSON-Request", body);
		client.addParam(PARAM_DATA, body);

		// execute request
		try {
			client.execute(RequestMethod.GET);
		} catch (Exception e) {
			Log.e("Request-Exception:", e.toString());
		}

		int responseCode = client.getResponseCode();
		if (responseCode == 200) {
			// HTTP response is OK
			String response = client.getResponse();
			Log.i("Servlet-Response", response);

		} else {
			String response = client.getResponse();
			Log.i("Servlet-Response", response);
		}
	}
}
