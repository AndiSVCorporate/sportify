package com.msports.sportify.server;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.msports.sportify.client.SportifyService;
import com.msports.sportify.shared.DailyStepsData;
import com.msports.sportify.shared.DailyStepsEntry;
import com.msports.sportify.shared.DailyStepsEntryOfy;
import com.msports.sportify.shared.FieldVerifier;
import com.msports.sportify.shared.OfyUtil;
import com.msports.sportify.shared.Session;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class SportifyServiceImpl extends RemoteServiceServlet implements
SportifyService {

	public String greetServer(String input) throws IllegalArgumentException {
		// Verify that the input is valid. 
		if (!FieldVerifier.isValidName(input)) {
			// If the input is not valid, throw an IllegalArgumentException back to
			// the client.
			throw new IllegalArgumentException(
					"Name must be at least 4 characters long");
		}

		String serverInfo = getServletContext().getServerInfo();
		String userAgent = getThreadLocalRequest().getHeader("User-Agent");

		// Escape data from the client to avoid cross-site script vulnerabilities.
		input = escapeHtml(input);
		userAgent = escapeHtml(userAgent);

		return "Hello, " + input + "!<br><br>I am running " + serverInfo
				+ ".<br><br>It looks like you are using:<br>" + userAgent;
	}

	/**
	 * Escape an html string. Escaping data received from the client helps to
	 * prevent cross-site script vulnerabilities.
	 * 
	 * @param html the html string to escape
	 * @return the escaped string
	 */
	private String escapeHtml(String html) {
		if (html == null) {
			return null;
		}
		return html.replaceAll("&", "&amp;").replaceAll("<", "&lt;")
				.replaceAll(">", "&gt;");
	}

	@Override
	public int getInteger(int i) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		return i+1;
	}

	@Override
	public List<DailyStepsEntryOfy> getDailyStepsDataOfUser(String user)
			throws IllegalArgumentException {
		return OfyUtil.getDailyStepsOfUser(user);
//		return new DailyStepsData[]{new DailyStepsData(11, System.currentTimeMillis())};
	}

	@Override
	public List<Session> getSessionsOfUser(String user)
			throws IllegalArgumentException {
		return OfyUtil.getSessionsOfUser(user);
	}

}
