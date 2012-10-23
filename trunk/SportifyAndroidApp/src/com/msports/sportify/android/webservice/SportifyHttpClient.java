package com.msports.sportify.android.webservice;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;

import android.util.Log;

/**
 * Simple client for REST services.
 * 
 * @author Luke Lowrey
 * @author Robert Schenkenfelder (edited)
 * 
 */
public abstract class SportifyHttpClient<T> {

	protected static final String LOG_TAG = "runtastic";
	
	/**
	 * Timeout when the socket closes.<br />
	 * Constant value: 8 seconds (8000ms)
	 */
	public static final int TIMEOUT_SOCKET = 8000;
	/**
	 * Timeout when the connection closes.<br />
	 * Constant value: 10 seconds (10000ms)
	 */
	public static final int TIMEOUT_CONNECTION = 10000;

	private final ArrayList<NameValuePair> params;
	private final ArrayList<NameValuePair> headers;

	private final String url;

	private int responseCode;
	private String message;
	private Header[] responseHeaders;

	private HttpEntity entity;
	private final HttpParams httpParameters;

	public enum RequestMethod {
		GET, POST
	}

	private T response;

	public SportifyHttpClient(String url) {
		this.url = url;
		params = new ArrayList<NameValuePair>();
		headers = new ArrayList<NameValuePair>();

		httpParameters = new BasicHttpParams();

		HttpConnectionParams.setConnectionTimeout(httpParameters, TIMEOUT_CONNECTION);
		HttpConnectionParams.setSoTimeout(httpParameters, TIMEOUT_SOCKET);
	}

	public T getResponse() {
		return response;
	}

	public String getErrorMessage() {
		return message;
	}

	public int getResponseCode() {
		return responseCode;
	}

	public Header[] getResponseHeaders() {
		return responseHeaders;
	}

	public void addParam(String name, String value) {
		params.add(new BasicNameValuePair(name, value));
	}

	public void addHeader(String name, String value) {
		headers.add(new BasicNameValuePair(name, value));
	}

	public void setEntity(HttpEntity entity) {
		this.entity = entity;
	}

	public void execute(RequestMethod method) throws Exception {
		switch (method) {
		case GET:
			// add parameters
			String combinedParams = "";
			if (!params.isEmpty()) {
				combinedParams += "?";
				for (NameValuePair p : params) {
					String paramString = p.getName() + "=" + URLEncoder.encode(p.getValue(), "UTF-8");
					if (combinedParams.length() > 1) {
						combinedParams += "&" + paramString;
					} else {
						combinedParams += paramString;
					}
				}
			}

			HttpGet getRequest = new HttpGet(url + combinedParams);

			// add headers
			for (NameValuePair h : headers) {
				getRequest.addHeader(h.getName(), h.getValue());
			}
			executeRequest(getRequest, url);
			break;
		case POST:
			HttpPost postRequest = new HttpPost(url);

			// add headers
			for (NameValuePair h : headers) {
				postRequest.addHeader(h.getName(), h.getValue());
			}

			if (entity != null) {
				postRequest.setEntity(entity);
			} else if (!params.isEmpty()) {
				postRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			}
			executeRequest(postRequest, url);
			break;
		}
	}

	protected void executeRequest(HttpUriRequest request, String url) {
		HttpClient client = new DefaultHttpClient(httpParameters);

		HttpResponse httpResponse;
		try {
			httpResponse = client.execute(request);
			responseCode = httpResponse.getStatusLine().getStatusCode();
			message = httpResponse.getStatusLine().getReasonPhrase();
			responseHeaders = httpResponse.getAllHeaders();

			HttpEntity entity = httpResponse.getEntity();

			if (entity != null) {

				InputStream instream = entity.getContent();
				response = convertStream(instream);

				// Closing the input stream will trigger connection release
				instream.close();
			}

		} catch (ClientProtocolException e) {
			client.getConnectionManager().shutdown();
			Log.e(LOG_TAG, "RestClient error", e);
		} catch (IOException e) {
			responseCode = 503;
			client.getConnectionManager().shutdown();
			Log.e(LOG_TAG, "RestClient error", e);
		}
	}

	protected abstract T convertStream(InputStream is);
}
