package com.msports.sportify.android;

import com.msports.sportify.android.model.SessionModel;
import com.msports.sportify.android.session.SessionManager;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class SessionFragment extends Fragment {

	private View v;
	
	private TextView m_trimpView;
	private TextView m_speedView;
	private TextView m_distanceView;
	private TextView m_heartRateView;
	
	private SessionManager manager;
	
	public SessionFragment(MainActivity activity) {
		manager = new SessionManager(activity);
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.sessionbased, null);
		
		v = view;	
		
		m_trimpView = (TextView)view.findViewById(R.id.trimpview);
		m_speedView = (TextView)view.findViewById(R.id.speedview);
		m_distanceView = (TextView)view.findViewById(R.id.distanceview);
		m_heartRateView = (TextView)view.findViewById(R.id.heartrateview);

		return view;
	}
	
	public void updateView(final SessionModel model) {
		getActivity().runOnUiThread(new Runnable() {
			@Override
			public void run() {
				m_heartRateView.setText("" + model.getCurrentHeartRate());
				m_distanceView.setText("" + model.getDistance());
				m_speedView.setText("" + model.getSpeed());
			}
		});		
	}
}
