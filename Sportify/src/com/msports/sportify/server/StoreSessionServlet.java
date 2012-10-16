package com.msports.sportify.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gson.Gson;
import com.msports.sportify.shared.Session;

public class StoreSessionServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		String data = req.getParameter("data");
		Gson gson = new Gson();
		Session session = gson.fromJson(data, Session.class);

		resp.setContentType("text/plain");
		PrintWriter out = resp.getWriter();
		if(session != null) {
			out.println(session.getStepsToday() + " Schritte!");
			out.flush();

			SessionEntry.insert(session.getStepsToday(), new Date());
//			List<SessionEntry> sessions = SessionEntry.getEntries();
//			PrintWriter writer = resp.getWriter();
//			for (SessionEntry sessionEntry : sessions) {
//				writer.println(sessionEntry.getId() + " , "
//						+ sessionEntry.getStepsToday());
//			}
		}
		

		// UserService userService = UserServiceFactory.getUserService();
		// User user = userService.getCurrentUser();
		//
		//
		// if (user != null) {

		// writer.println("Hello, " + user.getNickname());

		// } else {
		// resp.sendRedirect(userService.createLoginURL(req.getRequestURI()));
		// }

	}

}
