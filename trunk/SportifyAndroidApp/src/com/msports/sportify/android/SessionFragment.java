package com.msports.sportify.android;

import gueei.binding.Binder;

import com.msports.sportify.android.model.SessionModel;
import com.msports.sportify.android.session.SessionManager;

import android.app.Activity;
import android.opengl.Visibility;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class SessionFragment extends Fragment implements OnClickListener{

	private View root;
	
	private TextView m_trimpView;
	private TextView m_speedView;
	private TextView m_distanceView;
	private TextView m_heartRateView;
	private TextView m_kcalView;
	
	private Button startButton;
	private Button stopButton;
	
	private SessionManager manager;

	
	public SessionFragment(MainActivity activity) {
		
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
//		View view = inflater.inflate(R.layout.sessionbased, null);
//		Binder.init();
		manager = new SessionManager((MainActivity)getActivity());
		root = Binder.bindView(this.getActivity(), Binder.inflateView(this.getActivity(), R.layout.sessionbased, container,
				false), SessionModel.getInstance(manager));
		
//		m_trimpView = (TextView)view.findViewById(R.id.trimpview);
//		m_speedView = (TextView)view.findViewById(R.id.speedview);
//		m_distanceView = (TextView)view.findViewById(R.id.distanceview);
//		m_heartRateView = (TextView)view.findViewById(R.id.heartrateview);
//		m_kcalView = (TextView)view.findViewById(R.id.kcalview);
		startButton = (Button)root.findViewById(R.id.startButton);
		startButton.setOnClickListener(this);
		stopButton = (Button)root.findViewById(R.id.stopButton);
		stopButton.setOnClickListener(this);

		return root;
	}
	
	public void updateView(final SessionModel model) {
//		getActivity().runOnUiThread(new Runnable() {
//			@Override
//			public void run() {
//				m_heartRateView.setText("" + model.getCurrentHeartRate());
//				m_distanceView.setText("" + model.getDistance());
//				m_speedView.setText("" + model.getSpeed());
//				m_kcalView.setText("" + model.getCalories());
//				m_trimpView.setText("" + model.getDuration()/1000);
//			}
//		});		
	}

	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.startButton) {
			manager.startSession();
			startButton.setVisibility(View.GONE);
			stopButton.setVisibility(View.VISIBLE);
			
		} else if (v.getId() == R.id.stopButton) {
			manager.stopSession();
			startButton.setVisibility(View.VISIBLE);
			stopButton.setVisibility(View.GONE);
		}
	}
	
	
}
