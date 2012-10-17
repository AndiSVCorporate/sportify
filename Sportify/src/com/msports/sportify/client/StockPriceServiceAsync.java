package com.msports.sportify.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface StockPriceServiceAsync {

	void getPrices(String[] symbols, AsyncCallback<StockPrice[]> callback);
	void getSessions(AsyncCallback<Session[]> callback);
	
}
