package com.msports.sportify.client;

import java.util.Date;
import java.util.List;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.dom.client.Document;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.Window.Location;
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
import com.google.gwt.visualization.client.Selection;
import com.google.gwt.visualization.client.VisualizationUtils;
import com.google.gwt.visualization.client.events.SelectHandler;
import com.google.gwt.visualization.client.visualizations.LineChart;
import com.google.gwt.visualization.client.visualizations.LineChart.Options;
import com.msports.sportify.shared.DailyStepsEntryOfy;

public class Sportify implements EntryPoint {



	/**
	 * Create a remote service proxy to talk to the server-side Greeting service.
	 */
	private final SportifyServiceAsync greetingService = GWT
			.create(SportifyService.class);

	private static final int REFRESH_INTERVAL = 5000; // ms
	private VerticalPanel mainPanel = new VerticalPanel();
	private FlexTable stocksFlexTable = new FlexTable();
	private Label lastUpdatedLabel = new Label();	
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

		System.out.println("Load fein");

		RootPanel rootPanel = RootPanel.get("stockList");
		if((rootPanel != null))		  
		{ 
			System.out.println("panel stocklist existiert");
			// Associate the Main panel with the HTML host page.
			mainPanel.setStyleName("center");
			rootPanel.add(mainPanel);

			// Create table for stock data.	
			stocksFlexTable.setText(0, 0, "Id");
			stocksFlexTable.setText(0, 1, "Steps");
			stocksFlexTable.setText(0, 2, "Day");
			stocksFlexTable.setText(0, 3, "Steps to Go");
			stocksFlexTable.setText(0, 4, "AverageHeartRate");

			// Add styles to elements in the stock list table.
			stocksFlexTable.setCellPadding(6);
			stocksFlexTable.getRowFormatter().addStyleName(0, "watchListHeader");
			stocksFlexTable.addStyleName("watchList");
			stocksFlexTable.getCellFormatter().addStyleName(0, 1, "watchListNumericColumn");
			stocksFlexTable.getCellFormatter().addStyleName(0, 2, "watchListNumericColumn");
			stocksFlexTable.getCellFormatter().addStyleName(0, 3, "watchListNumericColumn");   
			stocksFlexTable.getCellFormatter().addStyleName(0, 4, "watchListNumericColumn"); 
			mainPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);

			// Assemble Main panel.
			mainPanel.add(stocksFlexTable);
			mainPanel.setCellVerticalAlignment(stocksFlexTable, HasVerticalAlignment.ALIGN_MIDDLE);
			mainPanel.setCellHorizontalAlignment(stocksFlexTable, HasHorizontalAlignment.ALIGN_CENTER);
			stocksFlexTable.setWidth("400px");

			verticalPanel.setStyleName("center");					
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

			//Window.open(YOUR_URL_TO_OTHER_GWT_PAGE, "_self", ""); 
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

					linChart = new LineChart(createTable(null), createOptions());					
					linChart.addSelectHandler(createSelectHandler(linChart));
					linChart.setStyleName("center");
					panel.add(linChart);	
				}
			};

			// Load the visualization api, passing the onLoadCallback to be called
			// when loading is done.
			VisualizationUtils.loadVisualizationApi(onLoadCallback, LineChart.PACKAGE);		
		}
	}

	/**
	 * Generate random stock prices.
	 */
	private void refreshWatchList() {
		//Window.Location.assign(GWT.getHostPageBaseURL() + "SessionPage.html");
		final int rpcAntwort = 0;
		greetingService.getDailyStepsDataOfUser("testuser", new AsyncCallback<List<DailyStepsEntryOfy>>() {


			@Override
			public void onFailure(Throwable caught) {
				System.out.println("Fail");
			}


			@Override
			public void onSuccess(List<DailyStepsEntryOfy> result) {
				updateTable(result);
			}			

		});
	}

	private void updateTable(List<DailyStepsEntryOfy> result) {
		System.out.println(result.size());

		//update linchart
		if (linChart != null && result != null) {
			linChart.draw(createTable(result), createOptions());
		}

		//updateTable
		int sum = 0;
		int i = 0;
		for (DailyStepsEntryOfy res : result) {
			sum += res.getStepsToday();
			updateTable(res,i);
			i++;
		}		

		lblOverallSteps.setText(""+sum);   
		lblAverageSteps.setText("" + sum / result.size()); 

		// Display timestamp showing last refresh.
		lastUpdatedLabel.setText("Last update : "
				+ DateTimeFormat.getMediumDateTimeFormat().format(new Date()));

	}	

	private void updateTable(DailyStepsEntryOfy res, int row) {
		//Set steps into the table
		stocksFlexTable.setText(row+1, 1, "" + res.getStepsToday());		

		Date date = new Date(res.getDate());	
		StringBuffer buf = new StringBuffer(date.toString());		
		buf = new StringBuffer(buf.substring(buf.indexOf(" ")+1));
		String month = buf.substring(0, buf.indexOf(" "));
		StringBuffer buf2 = new StringBuffer(buf.substring(buf.indexOf(" ")+1));
		String day = buf2.substring(0, buf2.indexOf((" ")));
		String year = buf2.substring(buf2.lastIndexOf(" "));
		stocksFlexTable.setText(row+1, 2, "" + day + " " + month + " " + year);

	}

	/**
	 * Create options for the linechart
	 * @return the options
	 */
	private Options createOptions() {
		Options options = Options.create();
		options.setWidth(400);
		options.setHeight(240);
		options.setTitle("My Daily Activities");
		return options;
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


	private AbstractDataTable createTable(List<DailyStepsEntryOfy> result) {

		DataTable data = DataTable.create();
		data.addColumn(ColumnType.STRING, "");		
		data.addColumn(ColumnType.NUMBER, "Steps per day");
		if (result != null) {
			data.addRows(result.size());

			int i = 0;
			for(DailyStepsEntryOfy res : result) {
				data.setValue(i, 1, res.getStepsToday());
				//data.setValue(i,1,"Test");
				data.setValue(i,0,""+(i+1));	
				i++;
			}				
		}
		return data;
	}
}
