package com.msports.sportify.client;

import java.util.List;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
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
import com.google.gwt.visualization.client.VisualizationUtils;
import com.google.gwt.visualization.client.visualizations.LineChart;
import com.msports.sportify.shared.DailyStepsEntryOfy;

public class SessionPage implements EntryPoint {
	
	private VerticalPanel mainPanel = new VerticalPanel();
	private final Label lblAverageSteps = new Label("asdfasdf");
	private Anchor pedometer = new Anchor("Pedometer");
	private Anchor sessions = new Anchor("Sessions");
	private final VerticalPanel verticalPanel = new VerticalPanel();
	
	/**
	 * Create a remote service proxy to talk to the server-side Greeting service.
	 */
	private final SportifyServiceAsync greetingService = GWT
			.create(SportifyService.class);

	private static final int REFRESH_INTERVAL = 5000; // ms
	
	private FlexTable stocksFlexTable2 = new FlexTable();
	private Label lastUpdatedLabel = new Label();	
	private boolean firstIn = false;
	private final Label lblNewLabel = new Label("Steps overall: ");
	private final Label lblOverallSteps = new Label("");
	private final Label lblAverageStepsPer = new Label("Average Steps per Day:  ");

	private final HorizontalPanel horizontalPanel = new HorizontalPanel();

	private final HorizontalPanel horizontalPanel_1 = new HorizontalPanel();
	private LineChart linChart; 

	@Override
	public void onModuleLoad() {	
		
		
		if((RootPanel.get("sessionpage") != null))		  
		{ 			
			RootPanel rootPanel = RootPanel.get("sessionpage");		
		 	rootPanel.add(mainPanel);
			System.out.println("panel stocklist existiert");
			
			// Create table for stock data.	
			stocksFlexTable2.setText(0, 0, "Id");
			stocksFlexTable2.setText(0, 1, "Session");
			stocksFlexTable2.setText(0, 2, "Day");			

			// Add styles to elements in the stock list table.
			stocksFlexTable2.setCellPadding(6);
			stocksFlexTable2.getRowFormatter().addStyleName(0, "watchListHeader");
			stocksFlexTable2.addStyleName("watchList");
			stocksFlexTable2.getCellFormatter().addStyleName(0, 1, "watchListNumericColumn");
			stocksFlexTable2.getCellFormatter().addStyleName(0, 2, "watchListNumericColumn");			
			mainPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);

			// Assemble Main panel.
			mainPanel.add(stocksFlexTable2);
			mainPanel.setCellVerticalAlignment(stocksFlexTable2, HasVerticalAlignment.ALIGN_MIDDLE);
			mainPanel.setCellHorizontalAlignment(stocksFlexTable2, HasHorizontalAlignment.ALIGN_CENTER);
			stocksFlexTable2.setWidth("400px");

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
			
		

//			// Create a callback to be called when the visualization API
//			// has been loaded.
//			Runnable onLoadCallback = new Runnable() {
//				public void run() {
//					Panel panel = RootPanel.get();
//
//					linChart = new LineChart(createTable(null), createOptions());					
//					linChart.addSelectHandler(createSelectHandler(linChart));
//					panel.add(linChart);	
//				}
//			};

			// Load the visualization api, passing the onLoadCallback to be called
			// when loading is done.
			//VisualizationUtils.loadVisualizationApi(onLoadCallback, LineChart.PACKAGE);		 	
			
		} else if (RootPanel.get("startpage") != null) {
			RootPanel rootPanel = RootPanel.get("startpage");		
		 	rootPanel.add(mainPanel);			
			
			mainPanel.add(verticalPanel);
			verticalPanel.add(pedometer);
			pedometer.setHref("http://127.0.0.1:8888/Sportify.html?gwt.codesvr=127.0.0.1:9997");
			verticalPanel.add(sessions);
			sessions.setHref("http://127.0.0.1:8888/SessionPage.html?gwt.codesvr=127.0.0.1:9997");
			System.out.println("start page");
		} else {
			System.out.println("Nix existiert");
		}
		
		System.out.println("Loaded");
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
				//updateTable(result);
			}			

		});
	}
}
