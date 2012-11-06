package com.msports.sportify.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
import com.msports.sportify.shared.OfyUtil;
import com.msports.sportify.shared.Session;
import com.msports.sportify.shared.SessionEntry;

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
			out.println(session.getAvgHeartRate() + " HeartRate!");
			
			out.println(session.getAvgHeartRate() + " Session gespeichert von HTTP-Post!");
			List<Session> sessions = OfyUtil.getSessionsOfUser("testuser");
			for(Session s: sessions) {
				out.println(s.getStartTime());
			}
			
			out.flush();

			OfyUtil.insertSessionEntry(session);
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
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		String data = convertStreamToString(req.getInputStream());
		
		Gson gson = new Gson();
		Session session = gson.fromJson(data, Session.class);

		resp.setContentType("text/plain");
		PrintWriter out = resp.getWriter();
		if(session != null) {		
			
			out.flush();

			OfyUtil.insertSessionEntry(session);
		}
	}
	
	
	/**
	 * Reads data from a stream and returns them as a string.
	 *
	 * @param is
	 *            the input stream to read of
	 * @return the data which has been read as a string
	 */
	private static String convertStreamToString(InputStream is) {
		/*
		 * To convert the InputStream to String we use the
		 * BufferedReader.readLine() method. We iterate until the BufferedReader
		 * return null which means there's no more data to read. Each line will
		 * appended to a StringBuilder and returned as String.
		 */
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
//			Log.e("HTTP", "HTTP::convertStreamToString IOEX",e);
		} finally {
			try {
				is.close();
			} catch (IOException e) {
//				Log.e(Constants.TAG, "HTTP::convertStreamToString, finally catch IOEX", e);
			}
		}
		return sb.toString();
	}

}
