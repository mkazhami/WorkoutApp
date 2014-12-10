package com.example.workout;

import java.util.ArrayList;

import com.jjoe64.graphview.CustomLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphViewDataInterface;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.LineGraphView;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;

public class ViewHistoryExercise extends Activity{

	String exerName;
	ArrayList<Pair<String, String>> sets;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_history_exercise);
		
		Bundle bundle = this.getIntent().getExtras();
		exerName = bundle.getString("exerName");
		for(ExerciseRecord er : WorkoutObjects.recordList) {
			if(er.getName().equals(exerName)) {
				sets = er.getSets();
				for(Pair<String, String> pair : sets) {
					pair.getL().replaceAll("/", "");
				}
				break;
			}
		}
		
		createGraph();
	}
	
	private void createGraph() {
		int size = sets.size();
		GraphViewData[] data = new GraphViewData[size];
		for(int i = 0; i < size; i++) {
			Pair<String, String> pair = sets.get(i);
			data[i] = new GraphViewData(Double.parseDouble(pair.getL()), Double.parseDouble(pair.getR()));
		}
		GraphViewSeries exerciseGraphSeries = new GraphViewSeries(data);
		LinearLayout layout = (LinearLayout) findViewById(R.id.exerciseHistory);
		GraphView exerciseGraph = new LineGraphView(this, exerName + " history");
		exerciseGraph.addSeries(exerciseGraphSeries);
		exerciseGraph.setCustomLabelFormatter(new CustomLabelFormatter() {
	        @Override
	        public String formatLabel(double value, boolean isValueX) {
	            if (isValueX) {
	                int newVal = (int) value;
	                String year = "";
	                String month = ""; 
	                String day = "";
	                day += Integer.toString(newVal%100);
	                newVal = newVal / 100;
	                month += Integer.toString(newVal%100);
	                newVal = newVal / 100;
	                year = Integer.toString(newVal);
	                return day + "/" + month + "/" + year;
	            }
	            return "" + (int) value;
	        }
	    });
		layout.addView(exerciseGraph);
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
