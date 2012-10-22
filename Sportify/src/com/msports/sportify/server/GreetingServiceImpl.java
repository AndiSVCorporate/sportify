package com.msports.sportify.server;

import java.util.List;

import com.msports.sportify.client.GreetingService;
import com.msports.sportify.shared.DailyStepsData;
import com.msports.sportify.shared.DailyStepsEntry;
import com.msports.sportify.shared.FieldVerifier;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class GreetingServiceImpl extends RemoteServiceServlet implements
		GreetingService {

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
	public DailyStepsData[] getDailyStepsData()
			throws IllegalArgumentException {
		List<DailyStepsEntry> entries = DailyStepsEntry.getEntries();
		DailyStepsEntry entry = entries.get(0);
//		return (DailyStepsEntry[]) DailyStepsEntry.getEntries().toArray();
		return new DailyStepsData[]{new DailyStepsData(entry.getStepsToday(), entry.getDate().getTime())};
//		return new DailyStepsData[]{new DailyStepsData(11, System.currentTimeMillis())};
	}

	@Override
	public DailyStepsEntry getDailyStepsData1() throws IllegalArgumentException {
		return DailyStepsEntry.getEntries().get(0);
	}
}
