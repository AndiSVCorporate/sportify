package com.msports.sportify.shared;

import static com.msports.sportify.server.OfyService.ofy;

import java.util.List;

import com.googlecode.objectify.Query;

public class OfyUtil {
	
	public static List<DailyStepsEntryOfy> getDailyStepsOfUser(String user) {
		Query<DailyStepsEntryOfy> query = ofy().query(DailyStepsEntryOfy.class).filter("user", user).order("date");
		return query.list();
	}
	
	public static void insertDailyStepsEntry(DailyStepsData data) {
		DailyStepsEntryOfy entry = new DailyStepsEntryOfy();
		entry.setUser(data.getUser());
		entry.setStepsToday(data.getStepsToday());
		entry.setDate(data.getDate());
		
		ofy().put(entry);
	}
}
