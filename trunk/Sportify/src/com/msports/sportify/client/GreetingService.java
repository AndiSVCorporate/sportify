package com.msports.sportify.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.msports.sportify.shared.DailyStepsData;
/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("greet")
public interface GreetingService extends RemoteService {
	String greetServer(String name) throws IllegalArgumentException;
	int getInteger(int i) throws IllegalArgumentException;
	DailyStepsData[] getDailyStepsData() throws IllegalArgumentException;
//	DailyStepsEntry getDailyStepsData1() throws IllegalArgumentException;
}
