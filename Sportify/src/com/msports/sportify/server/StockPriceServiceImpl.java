package com.msports.sportify.server;

import com.msports.sportify.client.Session;
import com.msports.sportify.client.StockPrice;
import com.msports.sportify.client.StockPriceService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class StockPriceServiceImpl extends RemoteServiceServlet implements StockPriceService {

  private static final double MAX_PRICE = 100.0; // $100.00
  private static final double MAX_PRICE_CHANGE = 0.02; // +/- 2%

  public StockPrice[] getPrices(String[] symbols) {
    Random rnd = new Random();

    StockPrice[] prices = new StockPrice[symbols.length];
    for (int i=0; i<symbols.length; i++) {
      double price = rnd.nextDouble() * MAX_PRICE;
      double change = price * MAX_PRICE_CHANGE * (rnd.nextDouble() * 2f - 1f);

      prices[i] = new StockPrice(symbols[i], price, change);
    }

    return prices;
  }

@Override
public Session[] getSessions() {
	Session s[] = new Session[1];
	
	// Get a Calendar for current locale and time zone
    Calendar cal = Calendar.getInstance();  

    // Figure out what day of the year today is
    cal.setTime(new Date(1220227200));                        // Set to the current time
    int dayOfYear = cal.get(Calendar.DAY_OF_YEAR);  // What day of the year is it?    
    int month = cal.get(Calendar.MONTH) + 1;  // Query a different field
    int year = cal.get(Calendar.YEAR);  // Query a different field
    
    Random rnd = new Random();    
    int steps = rnd.nextInt(10) * 100;      
    
    Session session = new Session(10,20,steps);
    String str = "" + dayOfYear + "." + month + "." + year;
    session.setDateString(new String(str));
    
	s[0] = session;
	return s;
}

}