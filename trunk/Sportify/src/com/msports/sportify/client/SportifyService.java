package com.msports.sportify.client;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.msports.sportify.shared.DailyStepsData;
import com.msports.sportify.shared.DailyStepsEntryOfy;
import com.msports.sportify.shared.Session;
/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("service")
public interface SportifyService extends RemoteService {
	String greetServer(String name) throws IllegalArgumentException;
	int getInteger(int i) throws IllegalArgumentException;
	List<DailyStepsEntryOfy> getDailyStepsDataOfUser(String user) throws IllegalArgumentException;
	List<Session> getSessionsOfUser(String user) throws IllegalArgumentException;
}
