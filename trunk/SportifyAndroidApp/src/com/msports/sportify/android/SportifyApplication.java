package com.msports.sportify.android;

import gueei.binding.Binder;
import android.app.Application;

public class SportifyApplication extends Application{

	
	@Override
	public void onCreate() {
		// initialize the binder
		Binder.init(this);
		super.onCreate();
	}
}
