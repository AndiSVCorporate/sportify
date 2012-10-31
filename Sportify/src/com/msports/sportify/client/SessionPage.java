package com.msports.sportify.client;

import java.util.Date;
import java.util.List;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.layout.client.Layout;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasHorizontalAlignment.HorizontalAlignmentConstant;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.visualization.client.AbstractDataTable;
import com.google.gwt.visualization.client.DataTable;
import com.google.gwt.visualization.client.Selection;
import com.google.gwt.visualization.client.VisualizationUtils;
import com.google.gwt.visualization.client.AbstractDataTable.ColumnType;
import com.google.gwt.visualization.client.events.SelectHandler;
import com.google.gwt.visualization.client.events.SelectHandler.SelectEvent;
import com.google.gwt.visualization.client.visualizations.LineChart;
import com.google.gwt.visualization.client.visualizations.LineChart.Options;
import com.msports.sportify.shared.DailyStepsEntryOfy;
import com.msports.sportify.shared.Session;

public class SessionPage implements EntryPoint {

	private VerticalPanel mainPanel = new VerticalPanel();
	private Label lblDistanceOverall= new Label("asdf");
	private Label lblCalorieOverall= new Label("asdf");
	private Label lblDurationOverall= new Label("asdf");
	private Anchor pedometer = new Anchor("Pedometer");
	private Anchor sessions = new Anchor("Sessions");
	private final VerticalPanel verticalPanel = new VerticalPanel();

	/**
	 * Create a remote service proxy to talk to the server-side Greeting service.
	 */
	private final SportifyServiceAsync greetingService = GWT
			.create(SportifyService.class);

	private static final int REFRESH_INTERVAL = 60000; // ms

	private FlexTable stocksFlexTable = new FlexTable();
	private Label lastUpdatedLabel = new Label();	
	private boolean firstIn = false;	

	private final HorizontalPanel horizontalPanel = new HorizontalPanel();	
	private LineChart linChart; 

	@Override
	public void onModuleLoad() {
		
		//Check if sessionpage exists
		if((RootPanel.get("sessionpage") != null))		  
		{ 			
			RootPanel rootPanel = RootPanel.get("sessionpage");			

			System.out.println("panel stocklist existiert");
			mainPanel.setStyleName("center");
			
			// Create table for stock data.	
			stocksFlexTable.setText(0, 0, "Id");
			stocksFlexTable.setText(0, 1, "Date");
			stocksFlexTable.setText(0, 2, "Trimp");		
			stocksFlexTable.setText(0, 3, "Avg Heartrate");	
			stocksFlexTable.setText(0, 4, "Details");	

			// Add styles to elements in the stock list table.
			stocksFlexTable.setCellPadding(6);
			stocksFlexTable.getRowFormatter().addStyleName(0, "watchListHeader");
			stocksFlexTable.addStyleName("watchList");
			stocksFlexTable.getCellFormatter().addStyleName(0, 1, "watchListNumericColumn");
			stocksFlexTable.getCellFormatter().addStyleName(0, 2, "watchListNumericColumn");
			stocksFlexTable.getCellFormatter().addStyleName(0, 3, "watchListNumericColumn");			
			stocksFlexTable.getCellFormatter().addStyleName(0, 4, "watchListRemoveColumn");

			// Assemble Main panel.
			mainPanel.add(stocksFlexTable);			
			stocksFlexTable.setWidth("400px");

			
			mainPanel.add(lblDistanceOverall);
			mainPanel.add(lblDurationOverall);
			
			//verticalPanel.setStyleName("center");

			mainPanel.add(lastUpdatedLabel);
			mainPanel.add(lblCalorieOverall);

			rootPanel.add(mainPanel);		
			
			// Create a callback to be called when the visualization API
			// has been loaded.
			Runnable onLoadCallback = new Runnable() {
				public void run() {
					Panel panel = RootPanel.get("sessionpage");

					linChart = new LineChart(createTable(null), createOptions());								
					linChart.addSelectHandler(createSelectHandler(linChart));
					linChart.setStyleName("center");					
					refreshSession();
					panel.add(linChart);	
				}
			};

			// Load the visualization api, passing the onLoadCallback to be called
			// when loading is done.
			VisualizationUtils.loadVisualizationApi(onLoadCallback, LineChart.PACKAGE);		 
			//refreshSession();
			Timer refreshTimer = new Timer() {
				@Override
				public void run() {
					refreshSession();
				}
			};
			
			if (! firstIn) {
				refreshTimer.scheduleRepeating(REFRESH_INTERVAL);
				firstIn = true;    	
			}

		} else if (RootPanel.get("startpage") != null) {			
			RootPanel rootPanel = RootPanel.get("startpage");			

			mainPanel.add(verticalPanel);
			verticalPanel.add(pedometer);						
			pedometer.setHref("http://" + Window.Location.getHost() + "/Sportify.html");			
			verticalPanel.add(sessions);			
			sessions.setHref("http://" + Window.Location.getHost() + "/SessionPage.html");

			verticalPanel.setStyleName("center");

			mainPanel.setStyleName("center");
			rootPanel.add(mainPanel);				
			System.out.println("start page");
		} else {
			System.out.println("Nix existiert");
		}

		System.out.println("Loaded");
	}

	/**
	 * Generate random stock prices.
	 */
	private void refreshSession() {		
		greetingService.getSessionsOfUser("testuser", new AsyncCallback<List<Session>>() {
			@Override
			public void onFailure(Throwable caught) {
				System.out.println("Fail");
			}


			@Override
			public void onSuccess(List<com.msports.sportify.shared.Session> result) {
				System.out.println("Success");
				updateTable(result);
			}	

		});
	}


	private void updateTable(List<Session> result) {
		System.out.println(result.size());

		//update linchart
		if (linChart != null && result != null) {
			linChart.draw(createTable(result), createOptions());
		}

		//updateTable	
		float duration = 0;
		float distance = 0;
		float calorie = 0;
		int i = 0;
		for (Session res : result) {			
			updateTable(res,i);
			duration += res.getDuration();
			distance += res.getDistance();
			calorie += res.getCalories();			
			i++;
		}		
		
		lblDistanceOverall.setText("Distance overall: " + distance + "\nAverage Distance: " + distance/i);
		lblDurationOverall.setText("Duration overall: " + distance + "\nAverage Duration: " + duration/i);
		lblCalorieOverall.setText("Calories overall: " + distance + "\nAverage Calories: " + calorie/i);


		// Display timestamp showing last refresh.
		lastUpdatedLabel.setText("Last update : "
				+ DateTimeFormat.getMediumDateTimeFormat().format(new Date()));

	}

	private void updateTable(Session res, int row) {
		//Set steps into the table 
		stocksFlexTable.setText(row+1, 0, "" + (row+1));	

		Date date = new Date(res.getStartTime());	
		StringBuffer buf = new StringBuffer(date.toString());		
		buf = new StringBuffer(buf.substring(buf.indexOf(" ")+1));
		String month = buf.substring(0, buf.indexOf(" "));
		StringBuffer buf2 = new StringBuffer(buf.substring(buf.indexOf(" ")+1));
		String day = buf2.substring(0, buf2.indexOf((" ")));
		String year = buf2.substring(buf2.lastIndexOf(" "));
		stocksFlexTable.setText(row+1, 1, "" + day + " " + month + " " + year);
		stocksFlexTable.setText(row+1, 2, "" + res.getTrimpScore());
		stocksFlexTable.setText(row+1, 3, "" + res.getAvgHeartRate());
		stocksFlexTable.setText(row+1, 4, "" + res.getDistance());

		Button sessionDetail = new Button("->");		
		sessionDetail.setTitle("" + res.getId());
		System.out.println(sessionDetail.getHTML());

		sessionDetail.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {		      
				Button b =  (Button)event.getSource(); 
				System.out.println(b.getTitle());
				Window.Location.assign(GWT.getHostPageBaseURL() + "SessionDetail.html?id=" + b.getTitle());
			}
		});
		sessionDetail.addStyleDependentName("remove");	    
		stocksFlexTable.setWidget(row+1, 4, sessionDetail);
	}		


	/**
	 * Create options for the linechart
	 * @return the options
	 */
	private Options createOptions() {
		Options options = Options.create();
		options.setWidth(400);
		options.setHeight(240);
		options.setTitle("All Sessions");
		options.setSmoothLine(true);		
		return options;
	}


	/**
	 * Inserts the data for the session
	 * @param session
	 * @return
	 */
	private AbstractDataTable createTable(List<Session> session) {
		DataTable data = DataTable.create();
		data.addColumn(ColumnType.STRING, "");		
		data.addColumn(ColumnType.NUMBER, "Trimp");
		data.addColumn(ColumnType.NUMBER, "Distance");

		if (session != null) {
			data.addRows(session.size());

			int i = 0;
			for(Session ses : session) {
				data.setValue(i, 1, ses.getTemperature());
				data.setValue(i, 2, 55);
				//data.setValue(i,1,"Test");
				data.setValue(i,0,""+(i+1));	
				i++;
			}				
		}
		return data;
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
}
