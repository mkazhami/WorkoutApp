package com.example.workout;

import java.sql.Date;
import java.util.ArrayList;
import java.util.GregorianCalendar;

import org.achartengine.ChartFactory;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import com.jjoe64.graphview.BarGraphView;
import com.jjoe64.graphview.CustomLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphViewDataInterface;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.LineGraphView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.Toast;

public class ViewHistoryExercise extends Activity{

	String exerName;
	ArrayList<Pair<String, String>> sets;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_history_exercise);
		
		sets = new ArrayList<Pair<String, String>>();
		
		Bundle bundle = this.getIntent().getExtras();
		exerName = bundle.getString("exerName");
		for(ExerciseRecord er : WorkoutObjects.recordList) {
			if(er.getName().equals(exerName)) {
				for(Pair<String, String> pair : er.getSets()) {
					sets.add(new Pair<String, String>(pair.getL(), pair.getR()));
				}
				for(Pair<String, String> pair : sets) {
					//pair.setL(pair.getL().replaceAll("/", ""));
				}
				break;
			}
		}
		
		createGraph();
	}
	
	private void createGraph() {
		int size = sets.size();
		String[] firstDate = null;
		String[] lastDate = null;
		double maxWeight = 0;
		
		
		//GraphViewData[] data = new GraphViewData[size];
		TimeSeries series = new TimeSeries("Line");
		for(int i = 0; i < size; i++) {
			Pair<String, String> pair = sets.get(i);
			String[] strDate = pair.getL().split("/");
			if(i == 0) firstDate = strDate;
			if(i == size - 1) lastDate = strDate;
			if(Double.parseDouble(pair.getR()) > maxWeight) maxWeight = Double.parseDouble(pair.getR());
			@SuppressWarnings("deprecation")
			Date date = new Date(Integer.parseInt(strDate[2]) - 1900,
								 Integer.parseInt(strDate[1]),
								 Integer.parseInt(strDate[0]));
			series.add(date, Double.parseDouble(pair.getR()));
		}
		
		
		java.util.Date minDate = (java.util.Date) new GregorianCalendar(Integer.parseInt(firstDate[2]) - 1900,
				 							Integer.parseInt(firstDate[1]),
				 							Integer.parseInt(firstDate[0])).getTime();
	    java.util.Date maxDate = (java.util.Date) new GregorianCalendar(Integer.parseInt(lastDate[2]) - 1900,
				 							Integer.parseInt(lastDate[1]),
				 							Integer.parseInt(lastDate[0])).getTime();
	    double THREEDAYS = 81300000 *3;
		double minX = minDate.getTime() - THREEDAYS;
		double maxX = maxDate.getTime() + THREEDAYS;
	    
		XYMultipleSeriesDataset dataSet = new XYMultipleSeriesDataset();
		dataSet.addSeries(series);
		XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();
		mRenderer.setPanLimits(new double[] {minX, maxX, 0, maxWeight});
		mRenderer.setZoomInLimitX(81300000);
		mRenderer.setZoomInLimitY(maxWeight/2);
		XYSeriesRenderer renderer = new XYSeriesRenderer();
		mRenderer.setShowGrid(true);
		mRenderer.addSeriesRenderer(renderer);
		mRenderer.setLabelsTextSize(25);
		LinearLayout layout = (LinearLayout) findViewById(R.id.exerciseHistoryGraph);
		layout.addView(ChartFactory.getTimeChartView(this, dataSet, mRenderer, "MM/dd/yyyy"));
	}
	
	public class GraphViewData implements GraphViewDataInterface {
	    private double x,y;

	    public GraphViewData(double x, double y) {
	        this.x = x;
	        this.y = y;
	    }

	    @Override
	    public double getX() {
	        return this.x;
	    }

	    @Override
	    public double getY() {
	        return this.y;
	    }
	}
}
