package com.msports.sportify.android.webservice;

import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;

import android.util.Log;

import com.google.gson.Gson;
import com.msports.sportify.android.model.SessionModel;
import com.msports.sportify.android.webservice.SportifyHttpClient.RequestMethod;

public class SportifyWebService {

	public static final String STORE_SESSION_URL = "http://sportify-msports.appspot.com/storeSession";
	public static final String PARAM_DATA = "data";

	public static void sendSessionStoringRequest(SessionModel model){
		RestClient client = new RestClient(STORE_SESSION_URL);
		Session session = new Session();
		session.setStartTime(model.startTime.get());
		session.setDuration(model.duration.get());
		session.setAvgHeartRate(model.avgHeartRate.get());
		session.setCalories((int) model.calories.get().intValue());
		session.setDistance((int) model.distance.get().intValue());
		session.setMaxHeartRate(model.maxHeartRate.get());
		session.setTemperature(model.temperature.get());
		session.setTrimpScore((int) model.trimpScore.get().intValue());
		session.setHeartRateTrace(WSUtils.encodeHeartRateTraceToString(model.heartRateTrace));

		Gson gson = new Gson();
		String json = gson.toJson(session);
		String body = json.toString();
		Log.i("JSON-Request", body);
//		client.addParam(PARAM_DATA, body);
		
		// set body and execute request
		try {
			StringEntity bodyEntity = new StringEntity(body, "UTF-8");
			bodyEntity.setContentEncoding("UTF-8");	
			client.setEntity(bodyEntity);
			client.execute(RequestMethod.POST);
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
			Log.i("Servlet-Response", responseCode + " " + response);
		}
	}
}
