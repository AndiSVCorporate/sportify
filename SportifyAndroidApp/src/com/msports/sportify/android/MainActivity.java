package com.msports.sportify.android;


import java.util.Date;

import com.msports.sportify.android.model.PedometerModel;
import com.msports.sportify.preferences.PedometerSettings;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.text.format.DateFormat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MainActivity extends FragmentActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide fragments for each of the
     * sections. We use a {@link android.support.v4.app.FragmentPagerAdapter} derivative, which will
     * keep every loaded fragment in memory. If this becomes too memory intensive, it may be best
     * to switch to a {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;
    
    public static final String PREFS_NAME = "PedPref";
    
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private PedometerSettings mPedometerSettings;
    private PedometerModel m_model;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        // Create the adapter that will return a fragment for each of the three primary sections
        // of the app.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        
        //load preferences
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        int totalSteps = settings.getInt("totalSteps", 0);
        long prevOpening = settings.getLong("prevOpening", new Date().getTime());
        
        //check if the app is opened the first time of this day
        CharSequence s = DateFormat.format("EEEE, MMMM d, yyyy ", prevOpening);
        CharSequence s2 = DateFormat.format("EEEE, MMMM d, yyyy ", new Date().getTime());
        
        boolean firstOpeningToday = !s.equals(s2);
        int stepsToday = settings.getInt("stepsToday", 0);
        
        //load the user defined settings
        settings = PreferenceManager.getDefaultSharedPreferences(this);
        mPedometerSettings = new PedometerSettings(settings);
        
        m_model = new PedometerModel(totalSteps, firstOpeningToday, stepsToday);

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to one of the primary
     * sections of the app.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
        	if(i == 0) {
        		UploadFragment fragm = new UploadFragment();
        		return fragm;
        	} else if (i == 1) {
        		PedometerFragment fragm = new PedometerFragment(m_model);
        		return fragm;
        	}
        	
            Fragment fragment = new DummySectionFragment();
            Bundle args = new Bundle();
            args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, i + 1);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0: return getString(R.string.title_section1).toUpperCase();
                case 1: return getString(R.string.title_section2).toUpperCase();
                case 2: return getString(R.string.title_section3).toUpperCase();
            }
            return null;
        }
    }
    
    /* Creates the menu items */
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.clear();
      
        menu.add(0, 8, 0, "Settings")
        .setIntent(new Intent(this, com.msports.sportify.preferences.Settings.class));
        return true;
    }
    
    @Override
	protected void onDestroy() {
		super.onDestroy();
		savePreferences();		
	}
	
	public void savePreferences() {
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		m_model.savePreferences(settings);
	}	

    /**
     * A dummy fragment representing a section of the app, but that simply displays dummy text.
     */
    public static class DummySectionFragment extends Fragment {
        public DummySectionFragment() {
        }

        public static final String ARG_SECTION_NUMBER = "section_number";

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            TextView textView = new TextView(getActivity());
            textView.setGravity(Gravity.CENTER);
            Bundle args = getArguments();
            textView.setText(Integer.toString(args.getInt(ARG_SECTION_NUMBER)));
            return textView;
        }
    }
}
