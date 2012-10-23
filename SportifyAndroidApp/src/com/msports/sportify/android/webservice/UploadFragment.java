package com.msports.sportify.android.webservice;

import com.google.gson.Gson;
import com.msports.sportify.android.R;
import com.msports.sportify.android.R.id;
import com.msports.sportify.android.R.layout;
import com.msports.sportify.android.webservice.SportifyHttpClient.RequestMethod;

import android.os.Bundle;
import android.preference.EditTextPreference;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class UploadFragment extends Fragment implements OnClickListener{

	private Button storeBtn;
	private EditText stepsTodayEditText;
	private View v;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.upload, null);
		storeBtn = (Button) view.findViewById(R.id.storeSessionButton);
		stepsTodayEditText = (EditText) view.findViewById(R.id.editText1);
		
		storeBtn.setOnClickListener(this);
		v = view;
		return view;
	}

	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.storeSessionButton) {
			stepsTodayEditText = (EditText)getActivity().findViewById(R.id.editText1);
//			stepsTodayEditText = (EditText)v.findViewById(R.id.editText1);
			String numberAsString = stepsTodayEditText.getText().toString();
			
			int steps = new Integer(numberAsString);
			
			sendStoringRequest(steps);
			
		}
	}
	
	public static final String STORE_SESSION_URL = "http://sportify-msports.appspot.com/storeSession";
	public static final String PARAM_DATA = "data";
	
	private void sendStoringRequest(int steps) {
		RestClient client = new RestClient(STORE_SESSION_URL);
		Session session = new Session(steps, System.currentTimeMillis(), 10);
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
			
		}
	}
}
