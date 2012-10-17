package com.msports.sportify.android;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class SessionFragment extends Fragment {

	private View v;
	
	public SessionFragment() {
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.sessionbased, null);
		
		v = view;	
        
		return view;
	}
}
