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
	
	public static final int CUSTOMIZE_WORKOUT = 1;
	public static String FOLDER_NAME;
	public final static String FILE_NAME_PREFIX = "workout";
	
	private WorkoutList workouts;
	Button addWorkout;
	
	private static File workoutFolder;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		FOLDER_NAME = getBaseContext().getFilesDir() + "/Workout_Info/";
		workoutFolder = new File(FOLDER_NAME);
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
				startActivityForResult(intent, CUSTOMIZE_WORKOUT);
			}
			
		});
	}
	
	/*private void createWorkoutDialog() {
		// Create alert dialog to get new layout filename
		AlertDialog.Builder getWorkoutName = new AlertDialog.Builder(this);
		final View view = this.getLayoutInflater().inflate(R.layout.name_workout, null);
		getWorkoutName.setTitle("Give your workout a name");
		
		// sets up an EditText for the user to input the name

		getWorkoutName.setView(view);
		
		getWorkoutName.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int whichButton) {
				// hide the keyboard
				InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
			    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

			    //start the CreateLayout activity
				String workoutName = ((EditText)view.findViewById(R.id.nameWorkoutField)).getText().toString();
				/*boolean exists = false;
				for ( File layout : layoutFiles) {
					if (layout.getName().equals(fileName+".layout")) {
						exists = true;
						break;
					}
				}
				boolean exists = false;
				if (workouts.getWorkout(workoutName) != null) exists = true;
				
				if (exists) {
					// duplicate filename
					Toast.makeText(getApplicationContext(), "Workout '"+workoutName+"' already exists", Toast.LENGTH_SHORT).show();
				}
				else if (workoutName.equals("")) {
					// blank name provided
					Toast.makeText(getApplicationContext(), "Cannot create a workout without a name", Toast.LENGTH_SHORT).show();
				}
				else {
					Intent intent = new Intent(MainActivity.this, CustomizeWorkout.class);
					intent.putExtra("workoutName", workoutName);
					startActivityForResult(intent, CUSTOMIZE_WORKOUT);
				}
			  }
		});
		
		getWorkoutName.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			  public void onClick(DialogInterface dialog, int whichButton) {
			  }
		});

		// create and show the alert, show the keyboard, and get the focus on the editText
		AlertDialog al = getWorkoutName.create();
//		al.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
		al.show();
//		view.findViewById(R.id.layoutName).requestFocus();
	}*/
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case CUSTOMIZE_WORKOUT: 
			
		}
	}

}
