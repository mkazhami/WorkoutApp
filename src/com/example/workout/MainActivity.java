package com.example.workout;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	
	private WorkoutList workouts;
	Button addWorkout;
	
	private static File workoutFolder;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		WorkoutObjects.exerciseNamesList = new ArrayList<String>();
		WorkoutObjects.exerciseList = new ArrayList<Exercise>();
		
		// TEST EXERCISES - DELETE ONCE YOU GET A NEXUS CAUSE THE VIRTUAL DEVICE IS FUCKED
		Exercise e1 = new Exercise();
		e1.setName("Pushup"); e1.setReps(5); e1.setSets(5);
		Exercise e2 = new Exercise();
		e2.setName("Chinup"); e2.setReps(100); e2.setSets(34);
		WorkoutObjects.exerciseList.add(e1); WorkoutObjects.exerciseList.add(e2);
		WorkoutObjects.exerciseNamesList.add("Pushup"); WorkoutObjects.exerciseNamesList.add("Chinup");
		// TEST EXERCISES

		WorkoutObjects.FOLDER_NAME = getBaseContext().getFilesDir() + "/Workout_Info/";
		workoutFolder = new File(WorkoutObjects.FOLDER_NAME);
		if (!(workoutFolder.exists())) {
			workoutFolder.mkdirs();
		}
		else {
			
		}
		
		
		workouts = WorkoutList.getInstance();
		
		addWorkout = (Button) findViewById(R.id.addWorkout);
		setUpListeners();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	private void setUpListeners(){
		addWorkout.setOnClickListener(new OnClickListener(){
			public void onClick(View view){
				Intent intent = new Intent(MainActivity.this, CustomizeWorkout.class);
				startActivityForResult(intent, WorkoutObjects.CUSTOMIZE_WORKOUT);
			}
			
		});
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case WorkoutObjects.CUSTOMIZE_WORKOUT: 
			
		}
	}

}
