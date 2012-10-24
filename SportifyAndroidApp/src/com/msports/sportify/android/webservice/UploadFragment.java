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
			
//			int steps = new Integer(numberAsString);
			
//			SportifyWebService.sendSessionStoringRequest();
			
		}
	}
	
	
}
