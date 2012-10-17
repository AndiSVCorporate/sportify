package com.msports.sportify.android;

import com.msports.sportify.android.sensors.SensorFactoryManager;
import com.msports.sportify.android.sensors.steps.StepData;
import com.msports.sportify.android.sensors.steps.StepListener;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class SessionFragment extends Fragment implements StepListener {

	private View v;
	
	private TextView m_trimpView;
	private TextView m_speedView;
	private TextView m_distanceView;
	
	public SessionFragment() {
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.sessionbased, null);
		
		v = view;	
		
		m_trimpView = (TextView)view.findViewById(R.id.trimpview);
		m_speedView = (TextView)view.findViewById(R.id.speedview);
		m_distanceView = (TextView)view.findViewById(R.id.distanceview);
		
		SensorFactoryManager.getInstance(getActivity()).registerStepListener(this);
        
		return view;
	}

	@Override
	public void onUpdateStepSensor(StepData _data) {
		m_speedView.setText(String.valueOf(_data.getM_speed()));
		m_distanceView.setText(String.valueOf(_data.getM_distance()));
	}
}
