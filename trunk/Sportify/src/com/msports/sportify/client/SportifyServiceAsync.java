package com.msports.sportify.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.msports.sportify.shared.DailyStepsData;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface SportifyServiceAsync {
	void greetServer(String input, AsyncCallback<String> callback)
			throws IllegalArgumentException;
	void getInteger(int i, AsyncCallback<Integer> callback) throws IllegalArgumentException;
	void getDailyStepsData(AsyncCallback<DailyStepsData[]> callback) throws IllegalArgumentException;
//	void getDailyStepsData1(AsyncCallback<DailyStepsEntry> callback) throws IllegalArgumentException;
}
