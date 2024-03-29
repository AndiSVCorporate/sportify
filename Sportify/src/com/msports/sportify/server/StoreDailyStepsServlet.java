package com.msports.sportify.server;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.msports.sportify.shared.DailyStepsData;
import com.msports.sportify.shared.OfyUtil;

public class StoreDailyStepsServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		String data = req.getParameter("data");
		Gson gson = new Gson();
		DailyStepsData dailyStepsData = gson.fromJson(data, DailyStepsData.class);
		if(dailyStepsData.getDate() <= 0) {
			dailyStepsData.setDate(System.currentTimeMillis());
		}
		if(dailyStepsData.getUser() == null || (dailyStepsData.getUser().length() == 0)) {
			dailyStepsData.setUser("testuser");
		}
		
		resp.setContentType("text/plain");
		PrintWriter out = resp.getWriter();
		if(dailyStepsData != null) {
			out.println(dailyStepsData.getStepsToday() + " Schritte heute!");
			out.flush();
			OfyUtil.insertDailyStepsEntry(dailyStepsData);
		}
	}
}
