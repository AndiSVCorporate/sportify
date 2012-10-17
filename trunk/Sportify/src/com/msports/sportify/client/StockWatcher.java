package com.msports.sportify.client;

import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.visualization.client.AbstractDataTable;
import com.google.gwt.visualization.client.AbstractDataTable.ColumnType;

import com.google.gwt.visualization.client.DataTable;
import com.google.gwt.visualization.client.DataView;
import com.google.gwt.visualization.client.Selection;
import com.google.gwt.visualization.client.VisualizationUtils;
import com.google.gwt.visualization.client.events.SelectHandler;
import com.google.gwt.visualization.client.visualizations.LineChart;
import com.google.gwt.visualization.client.visualizations.LineChart.Options;
import com.google.gwt.visualization.client.visualizations.PieChart;

public class StockWatcher implements EntryPoint {

	private static final int REFRESH_INTERVAL = 5000; // ms
	private VerticalPanel mainPanel = new VerticalPanel();
	private FlexTable stocksFlexTable = new FlexTable();
	private Label lastUpdatedLabel = new Label();
	private ArrayList<String> stocks = new ArrayList<String>();  
	private ArrayList<String> sessions = new ArrayList<String>();  
	private StockPriceServiceAsync stockPriceSvc = GWT.create(StockPriceService.class);
	private boolean firstIn = false;
	private final Label lblNewLabel = new Label("Steps overall: ");
	private final Label lblOverallSteps = new Label("");
	private final Label lblAverageStepsPer = new Label("Average Steps per Day:  ");
	private final VerticalPanel verticalPanel = new VerticalPanel();
	private final HorizontalPanel horizontalPanel = new HorizontalPanel();
	private final Label lblAverageSteps = new Label("");
	private final HorizontalPanel horizontalPanel_1 = new HorizontalPanel();
	private LineChart linChart; 

	/**
	 * Entry point method.
	 */
	/* (non-Javadoc)
	 * @see com.google.gwt.core.client.EntryPoint#onModuleLoad()
	 */
	public void onModuleLoad() {	    
		// Create table for stock data.		
		stocksFlexTable.setText(0, 0, "Steps");
		stocksFlexTable.setText(0, 1, "Day");
		stocksFlexTable.setText(0, 2, "Steps to Go");
		stocksFlexTable.setText(0, 3, "AverageHeartRate");

		// Add styles to elements in the stock list table.
		stocksFlexTable.setCellPadding(6);
		stocksFlexTable.getRowFormatter().addStyleName(0, "watchListHeader");
		stocksFlexTable.addStyleName("watchList");
		stocksFlexTable.getCellFormatter().addStyleName(0, 1, "watchListNumericColumn");
		stocksFlexTable.getCellFormatter().addStyleName(0, 2, "watchListNumericColumn");
		stocksFlexTable.getCellFormatter().addStyleName(0, 3, "watchListNumericColumn");   
		mainPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);

		// Assemble Main panel.
		mainPanel.add(stocksFlexTable);
		mainPanel.setCellVerticalAlignment(stocksFlexTable, HasVerticalAlignment.ALIGN_MIDDLE);
		mainPanel.setCellHorizontalAlignment(stocksFlexTable, HasHorizontalAlignment.ALIGN_CENTER);
		stocksFlexTable.setWidth("400px");

		mainPanel.add(verticalPanel);
		verticalPanel.add(horizontalPanel);
		horizontalPanel.add(lblNewLabel);
		lblNewLabel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		lblOverallSteps.setStyleName("watchListGroundData");
		horizontalPanel.add(lblOverallSteps);

		verticalPanel.add(horizontalPanel_1);
		horizontalPanel_1.add(lblAverageStepsPer);
		lblAverageSteps.setStyleName("watchListGroundData");
		horizontalPanel_1.add(lblAverageSteps);
		mainPanel.add(lastUpdatedLabel);

		// Associate the Main panel with the HTML host page.
		RootPanel rootPanel = RootPanel.get("stockList");
		rootPanel.add(mainPanel);

		// Setup timer to refresh list automatically.
		refreshWatchList();
		Timer refreshTimer = new Timer() {
			@Override
			public void run() {
				refreshWatchList();
			}
		};
		if (! firstIn) {
			refreshTimer.scheduleRepeating(REFRESH_INTERVAL);
			firstIn = true;    	
		}

		// Create a callback to be called when the visualization API
		// has been loaded.
		Runnable onLoadCallback = new Runnable() {
			public void run() {
				Panel panel = RootPanel.get();

				// Create a pie chart visualization.
				//PieChart pie = new PieChart(createTable(), createOptions());
				linChart = new LineChart(createTable(), createOptions());					
				linChart.addSelectHandler(createSelectHandler(linChart));
				panel.add(linChart);

//				pie.addSelectHandler(createSelectHandler(pie));
//				panel.add(pie);
			}
		};

		// Load the visualization api, passing the onLoadCallback to be called
		// when loading is done.
		VisualizationUtils.loadVisualizationApi(onLoadCallback, LineChart.PACKAGE);		
	}

	/**
	 * Generate random stock prices.
	 */
	private void refreshWatchList() {
		// Initialize the service proxy.
		if (stockPriceSvc == null) {
			stockPriceSvc = GWT.create(StockPriceService.class);
		}
			

		//	    // Set up the callback object.
		//	    AsyncCallback<StockPrice[]> callback = new AsyncCallback<StockPrice[]>() {
		//	      public void onFailure(Throwable caught) {
		//	        // TODO: Do something with errors.
		//	      }
		//
		//	      public void onSuccess(StockPrice[] result) {
		//	        updateTable(result);	        
		//	      }
		//	    };

		// Set up the callback object.
		if (firstIn) {
			
		}
		
		AsyncCallback<Session[]> callback = new AsyncCallback<Session[]>() {
			public void onFailure(Throwable caught) {
				// TODO: Do something with errors.
			}

			public void onSuccess(Session[] result) {
				updateTable(result);				
				updateChart();  
			}
		};

		// Make the call to the stock price service.
		//stockPriceSvc.getPrices(stocks.toArray(new String[0]), callback);
		stockPriceSvc.getSessions(callback);
	}

	/**
	 * Update the Price and Change fields all the rows in the stock table.
	 *
	 * @param prices Stock data for all rows.
	 */
	private void updateTable(Session[] sessions) {
		for (int i = 0; i < sessions.length; i++) {
			updateTable(sessions[i]);
		}

		// Display timestamp showing last refresh.
		lastUpdatedLabel.setText("Last update : "
				+ DateTimeFormat.getMediumDateTimeFormat().format(new Date()));

	}
	
	/**
	 * Update the Chart	
	 */
	private void updateChart() {	
		if (linChart != null) {
			linChart.draw(createTable(), createOptions());	
		}			
	}

	private AbstractDataTable createTable2(Session[] sessions) {
		 DataTable data = DataTable.create();		    
		    data.addColumn(ColumnType.NUMBER, "Average Heart Rate");		   
		    data.addRows(10);
		  
		    for (int i = 0; i < data.getNumberOfRows(); i++) {
		    	data.setValue(i, 0, sessions[0].getStepsToday());
		    	//data.setValue(i, 1, i);
		    }
		    
		    return data;
		
	}

	/**
	 * Update a single row in the stock table.
	 *
	 * @param price Stock data for a single row.
	 */
	private void updateTable(Session session) {
		// Make sure the stock is still in the stock table.   

		// Populate the Price and Change fields with new data.
		stocksFlexTable.setText(1, 0, "" + session.getStepsToday());
		stocksFlexTable.setText(1, 1, "" + session.getDateString());    
		stocksFlexTable.setText(1, 2, "" + session.getTemperature());
		stocksFlexTable.setText(2, 0, "" + session.getStepsToday()); 
		stocksFlexTable.setText(2, 1, "" + session.getDateString()); 
		stocksFlexTable.setText(2, 2, "" + session.getTemperature());   
		lblOverallSteps.setText("300");   
		lblAverageSteps.setText("32,5");    
	}

	/**
	 * Update the Price and Change fields all the rows in the stock table.
	 *
	 * @param prices Stock data for all rows.
	 */
	private void updateTable(StockPrice[] prices) {
		for (int i = 0; i < prices.length; i++) {
			updateTable(prices[i]);
		}

		// Display timestamp showing last refresh.
		lastUpdatedLabel.setText("Last update : "
				+ DateTimeFormat.getMediumDateTimeFormat().format(new Date()));
	}


	/**
	 * Update a single row in the stock table.
	 *
	 * @param price Stock data for a single row.
	 */
	private void updateTable(StockPrice price) {
		// Make sure the stock is still in the stock table.
		if (!stocks.contains(price.getSymbol())) {
			return;
		}

		int row = stocks.indexOf(price.getSymbol()) + 1;

		// Format the data in the Price and Change fields.
		String priceText = NumberFormat.getFormat("#,##0.00").format(
				price.getPrice());
		NumberFormat changeFormat = NumberFormat.getFormat("+#,##0.00;-#,##0.00");
		String changeText = changeFormat.format(price.getChange());
		String changePercentText = changeFormat.format(price.getChangePercent());

		// Populate the Price and Change fields with new data.
		stocksFlexTable.setText(row, 1, priceText);
		Label changeWidget = (Label)stocksFlexTable.getWidget(row, 2);
		changeWidget.setText(changeText + " (" + changePercentText + "%)");

		// Change the color of text in the Change field based on its value.
		String changeStyleName = "noChange";
		if (price.getChangePercent() < -0.1f) {
			changeStyleName = "negativeChange";
		}
		else if (price.getChangePercent() > 0.1f) {
			changeStyleName = "positiveChange";
		}

		changeWidget.setStyleName(changeStyleName);
	}

	private Options createOptions() {
		Options options = Options.create();
		options.setWidth(400);
		options.setHeight(240);		
		//options.set3D(true);
		options.setTitle("My Daily Activities");
		return options;
	}

	private SelectHandler createSelectHandler(final PieChart chart) {
		return new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				String message = "";

				// May be multiple selections.
				JsArray<Selection> selections = chart.getSelections();

				for (int i = 0; i < selections.length(); i++) {
					// add a new line for each selection
					message += i == 0 ? "" : "\n";

					Selection selection = selections.get(i);

					if (selection.isCell()) {
						// isCell() returns true if a cell has been selected.

						// getRow() returns the row number of the selected cell.
						int row = selection.getRow();
						// getColumn() returns the column number of the selected cell.
						int column = selection.getColumn();
						message += "cell " + row + ":" + column + " selected";
					} else if (selection.isRow()) {
						// isRow() returns true if an entire row has been selected.

						// getRow() returns the row number of the selected row.
						int row = selection.getRow();
						message += "row " + row + " selected";
					} else {
						// unreachable
						message += "Pie chart selections should be either row selections or cell selections.";
						message += "  Other visualizations support column selections as well.";
					}
				}

				Window.alert(message);
			}
		};
	}

	private SelectHandler createSelectHandler(final LineChart chart) {
		return new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				String message = "";

				// May be multiple selections.
				JsArray<Selection> selections = chart.getSelections();

				for (int i = 0; i < selections.length(); i++) {
					// add a new line for each selection
					message += i == 0 ? "" : "\n";

					Selection selection = selections.get(i);

					if (selection.isCell()) {
						// isCell() returns true if a cell has been selected.

						// getRow() returns the row number of the selected cell.
						int row = selection.getRow();
						// getColumn() returns the column number of the selected cell.
						int column = selection.getColumn();
						message += "cell " + row + ":" + column + " selected";
					} else if (selection.isRow()) {
						// isRow() returns true if an entire row has been selected.

						// getRow() returns the row number of the selected row.
						int row = selection.getRow();
						message += "row " + row + " selected";
					} else {
						// unreachable
						message += "Pie chart selections should be either row selections or cell selections.";
						message += "  Other visualizations support column selections as well.";
					}
				}

				Window.alert(message);
			}
		};
	}

	private AbstractDataTable createTable() {
//		DataTable data = DataTable.create();
//		data.addColumn(ColumnType.STRING, "Task");
//		data.addColumn(ColumnType.NUMBER, "Heart Rate");
//		data.addRows(2);		
//		data.setValue(0, 0, "Work");		
//		data.setValue(0, 1, 14);
//		data.setValue(1, 0, "Sleep");
//		data.setValue(1, 1, 10);
//		return data;
		
		 DataTable data = DataTable.create();
		    //data.addColumn(ColumnType.STRING, "Task");
		    //data.addColumn(ColumnType.STRING, "Location");
		   
		    data.addColumn(ColumnType.STRING, "Average Heart Rate2");
		    data.addColumn(ColumnType.STRING, "Average Heartasdf Rate2");
		    data.addColumn(ColumnType.NUMBER, "Average Heart Rate");
		    data.addRows(10);
		  Random rnd = new Random();
		    for (int i = 0; i < data.getNumberOfRows(); i++) {
		    	data.setValue(i, 2, rnd.nextInt(100) * 10);
		    	data.setValue(i,1,"Test");
		    	data.setValue(i,0,""+i+1);
		    	//data.setValue(i, 1, i);
		    }
		    
		 
		   
		    
//		    data.setValue(0, 0, "Work");
//		    data.setValue(0, 1, "Mountain View");
//		    data.setValue(0, 2, 10);
//		    data.setValue(1, 0, "Commute");
//		    data.setValue(1, 1, "Route 17");
//		    data.setValue(1, 2, 4);
//		    data.setValue(2, 0, "Sleep");
//		    data.setValue(2, 1, "Santa Cruz");
//		    data.setValue(2, 2, 10);

		    // Data view -- read only, and no location column
//		    DataView result = DataView.create(data);
//		    result.setColumns(new int[]{0, 0});
		    
		    return data;
	}
}
