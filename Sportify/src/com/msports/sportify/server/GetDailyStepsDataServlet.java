package com.msports.sportify.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.msports.sportify.shared.DailyStepsEntry;

public class GetDailyStepsDataServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2L;

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

//		String data = req.getParameter("data");
		Gson gson = new Gson();
//		DailyStepsData dailyStepsData = gson.fromJson(data, DailyStepsData.class);

		resp.setContentType("text/plain");
		resp.setStatus(200);
		PrintWriter out = resp.getWriter();
//		out.print(req.getParameter("callback") + "(");
		List<DailyStepsEntry> entries = DailyStepsEntry.getEntries();
		if(entries != null) {
			String jsonData = gson.toJson(entries, List.class);
			out.print(jsonData);
//			out.print(");");
			out.flush();
		}
	}
}

