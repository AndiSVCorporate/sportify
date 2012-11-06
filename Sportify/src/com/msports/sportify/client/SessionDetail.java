package com.msports.sportify.client;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.visualization.client.AbstractDataTable;
import com.google.gwt.visualization.client.DataTable;
import com.google.gwt.visualization.client.Selection;
import com.google.gwt.visualization.client.VisualizationUtils;
import com.google.gwt.visualization.client.AbstractDataTable.ColumnType;
import com.google.gwt.visualization.client.events.SelectHandler;
import com.google.gwt.visualization.client.events.SelectHandler.SelectEvent;
import com.google.gwt.visualization.client.visualizations.LineChart;
import com.google.gwt.visualization.client.visualizations.LineChart.Options;
import com.msports.sportify.server.WSUtils;
import com.msports.sportify.shared.DailyStepsEntryOfy;
import com.msports.sportify.shared.HeartRateData;
import com.msports.sportify.shared.Session;

public class SessionDetail implements EntryPoint {

	/**
	 * Create a remote service proxy to talk to the server-side Greeting
	 * service.
	 */
	private final SportifyServiceAsync sportifyService = GWT
			.create(SportifyService.class);

	private static final int REFRESH_INTERVAL = 5000; // ms
	private VerticalPanel mainPanel = new VerticalPanel();
	private Label lastUpdatedLabel = new Label();
	private final Label lblNewLabel = new Label("Steps overall: ");
	private final Label lblOverallSteps = new Label("");
	private final Label lblAverageStepsPer = new Label(
			"Average Steps per Day:  ");
	private final VerticalPanel verticalPanel = new VerticalPanel();
	private final HorizontalPanel horizontalPanel = new HorizontalPanel();
	private final Label lblAverageSteps = new Label("");
	private final HorizontalPanel horizontalPanel_1 = new HorizontalPanel();
	private LineChart linChart;

	@Override
	public void onModuleLoad() {
		System.out.println("Load fein");

		RootPanel rootPanel = RootPanel.get("sessiondetail");
		
		Label lbl_startTime = new Label("Start Time: ");
		lbl_startTime.setStyleName("watchListGroundData");
		rootPanel.add(lbl_startTime, 29, 10);
		
		Label lblNewLabel_2 = new Label("...");
		lblNewLabel_2.setStyleName("watchList");
		rootPanel.add(lblNewLabel_2, 140, 10);
		if((rootPanel != null))		  
		{ 
			System.out.println("panel sessiondetail existiert");
			// Associate the Main panel with the HTML host page.

			rootPanel.add(mainPanel);
			
			// Create a callback to be called when the visualization API
						// has been loaded.
						Runnable onLoadCallback = new Runnable() {
							public void run() {
								
								
								long id = new Long(Window.Location.getParameter("id"));
								sportifyService.getSessionWithId(id, new AsyncCallback<Session>() {
									
									@Override
									public void onSuccess(Session result) {
										Panel panel = RootPanel.get();
										
										List<HeartRateData> hrTrace;
										try {
											hrTrace = result.getHeartRateTraceVector();
											System.out.println(hrTrace.size() + " Elemente");
											linChart = new LineChart(createTable(hrTrace), createOptions());					
											linChart.addSelectHandler(createSelectHandler(linChart));
											
											panel.add(linChart);	
										} catch (Exception e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}										
										
									}
									
									@Override
									public void onFailure(Throwable caught) {
										System.out.println("fail: " + caught.getMessage());
									}
								});
								
								
							}
						};

						// Load the visualization api, passing the onLoadCallback to be called
						// when loading is done.
						VisualizationUtils.loadVisualizationApi(onLoadCallback, LineChart.PACKAGE);	
		}
			

	}	
	

	/**
	 * Create options for the linechart
	 * 
	 * @return the options
	 */
	private Options createOptions() {
		Options options = Options.create();
		options.setWidth(1000);
		options.setHeight(240);
		options.setTitle("Traces");
		options.setSmoothLine(true);
		options.setPointSize(0);
		options.setEnableTooltip(true);
		return options;
	}

	private AbstractDataTable createTable(List<HeartRateData> hrTrace) {

		DataTable data = DataTable.create();
		data.addColumn(ColumnType.STRING, "text");
		data.addColumn(ColumnType.NUMBER, "Heart Rate [bpm]");
		if (hrTrace != null) {
			data.addRows(hrTrace.size());

			int i = 0;
			for (HeartRateData hr : hrTrace) {
				data.setValue(i,0, "" +hr.getRuntime());
				data.setValue(i, 1, hr.getHeartRate());
//				data.setValue(i, 0, "" + (i + 1));
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
