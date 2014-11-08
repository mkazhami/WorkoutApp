package com.example.workout;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	
	private WorkoutList workouts;
	Button addWorkout;
	
	
	private DragSortListView dslv;
	
	private static MyAdapter adapter;
	private static MatrixCursor cursor;
	
	
	//private static ArrayAdapter<String> adapter1;
	//private ListView list;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		WorkoutObjects.workoutList = WorkoutList.getInstance();
		WorkoutObjects.workoutNamesList = new ArrayList<String>();
		WorkoutObjects.exerciseNamesList = new ArrayList<String>();
		
		// TEST EXERCISES - DELETE ONCE YOU GET A NEXUS CAUSE THE VIRTUAL DEVICE IS FUCKED
		Exercise e1 = new Exercise();
		e1.setName("Chin-up"); e1.setReps("5"); e1.setSets("5");
		Exercise e2 = new Exercise();
		e2.setName("Push-up"); e2.setReps("100"); e2.setSets("34");
		WorkoutObjects.exerciseNamesList.add("Chin-up"); WorkoutObjects.exerciseNamesList.add("Push-up");
		// TEST EXERCISES

		WorkoutObjects.FOLDER_NAME = getBaseContext().getFilesDir().toString() + "/";
		File workoutFile = new File(WorkoutObjects.FOLDER_NAME + WorkoutObjects.WORKOUT_FILE_NAME);
		if (!(workoutFile.exists())) {
			try {
				workoutFile.createNewFile();
			} 
			catch (IOException e) { e.printStackTrace(); }
		}
		else {
			try {
				InputStream in = new FileInputStream(workoutFile);
				BufferedReader reader = new BufferedReader(new InputStreamReader(in));
				String line;
				while((line = reader.readLine()) != null) {
					Workout workout = Workout.convertFromJson(line);
					WorkoutObjects.workoutList.addWorkout(workout);
					WorkoutObjects.workoutNamesList.add(workout.getName());
				}
				reader.close();
				in.close();
			} 
			catch (FileNotFoundException e) {	e.printStackTrace(); } 
			catch (IOException e) {	e.printStackTrace(); }
		}
		
		dslv = (DragSortListView) findViewById(R.id.workoutList);
		String[] cols = {"name"};
        int[] ids = {R.id.text};
        adapter = new MyAdapter(this, R.layout.list_item_click_remove, null, cols, ids, 0);
        dslv.setAdapter(adapter);
        if (WorkoutObjects.workoutList != null) {
		    // populate the list of exercises
		    cursor = new MatrixCursor(new String[] {"_id", "name"});
		    int size = WorkoutObjects.workoutList.getSize();
			for (int i = 0; i < size; i++) {
				cursor.newRow()
					  .add(i)
		              .add(WorkoutObjects.workoutList.getWorkout(i).getName());
			}
		    adapter.changeCursor(cursor);
        }
		
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
				Bundle extras = new Bundle();
				extras.putInt("position", -1);
				intent.putExtras(extras);
				startActivityForResult(intent, WorkoutObjects.CUSTOMIZE_WORKOUT);
			}
		});
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
			case WorkoutObjects.CUSTOMIZE_WORKOUT: 
				if(resultCode == WorkoutObjects.BAD_RESULT) {
					Toast.makeText(this, "Workout not saved!" , Toast.LENGTH_SHORT).show();
				}
				else if (resultCode == WorkoutObjects.OK_RESULT) {
					Toast.makeText(this, "Workout saved!" , Toast.LENGTH_SHORT).show();
				}
		}
	}
	
	public static void updateList(Workout workout) {
		String name = workout.getName();
		int count = 0;
		//place the new workout name in the proper (alphabetical) position
		for(String workoutName : WorkoutObjects.workoutNamesList) {
			if(name.compareTo(workoutName) < 0) {
				WorkoutObjects.workoutNamesList.add(count, name);
				break;
			}
			count++;
		}
		//if the string is greater than all the strings in the list, place it at the end
		if(count >= WorkoutObjects.workoutNamesList.size()) {
			WorkoutObjects.workoutNamesList.add(name);
		}
	}
	
	public static void checkRemoved() {
		ArrayList<Integer> positions = adapter.getCursorPositions();
		ArrayList<String> newWorkoutList = new ArrayList<String>();
		if (cursor != null) {
			cursor.moveToFirst();
	        for (int i = 0; i < positions.size(); i++) {
	        	int cursorPosition = positions.get(i);
	        	cursor.moveToPosition(cursorPosition);
	        	String c = cursor.getString(1);
	        	newWorkoutList.add(c);
	        }
	        ArrayList<String> toRemove = new ArrayList<String>();
	        for(int i = 0; i < WorkoutObjects.workoutList.getSize(); i++) {
	        	if(!newWorkoutList.contains(WorkoutObjects.workoutList.getWorkout(i).getName())) {
	        		toRemove.add(WorkoutObjects.workoutList.getWorkout(i).getName());
	        	}
	        }
	        for(int i = 0; i < toRemove.size(); i++) {
	        	WorkoutObjects.workoutList.removeWorkout(toRemove.get(i));
	        	WorkoutObjects.workoutNamesList.remove(toRemove.get(i));
	        }
		}
		FileManagement.writeWorkoutFile();
	}
	
	public static void onEdit(Workout workout, int position) {
		ArrayList<Integer> positions = adapter.getCursorPositions();
		MatrixCursor newcursor = new MatrixCursor(new String[] {"_id", "name"});
		//int lastCursorPosition = cursor.getPosition();
		// copy over old cursor items to the new one.
		if (cursor != null) {
			cursor.moveToFirst();
	        for (int i = 0; i < positions.size(); i++) {
	        	int cursorPosition = positions.get(i);
	        	int listPosition = adapter.getListPosition(positions.get(i));
	        	// dont add to new cursor if item has been removed.
	        	if (listPosition == DragSortCursorAdapter.REMOVED) continue;
	        	cursor.moveToPosition(cursorPosition);
	        	String c = cursor.getString(1);
	        	// if its not a new exercise, check to see if the current cursor list position mapping
	        	// matches the one we are working with. If yes, set c to be the new exercise.
	        	if (position >= 0) {
	        		if (position == listPosition ) {
	        			c = workout.getName();
	        		}
	        	}
	        	newcursor.newRow()
	                    .add(listPosition)
	                    .add(c);
	        }
		}
        // add the new row
		//TODO: check if workout has already been added - don't allow duplicates
        if (position < 0 && workout != null) {
        	newcursor.newRow().add(newcursor.getCount()).add(workout.getName());
        }
        
        
        ArrayList<Workout> newWorkouts = new ArrayList<Workout>();
        newcursor.moveToFirst();
        for (int i = 0; i < newcursor.getCount(); i++) {
        	String wname = newcursor.getString(1);
        	Workout w = WorkoutObjects.workoutList.getWorkout(wname);
        	if (w != null) newWorkouts.add(w);
        	else if (i == newcursor.getCount() - 1) newWorkouts.add(workout);
        	if (!newcursor.moveToNext()) break;
        }
         
        adapter.changeCursor(newcursor); 
        cursor = newcursor;
		
        // modify workout to reflect the change
        WorkoutObjects.workoutList.setWorkouts(newWorkouts);
        
	}
	
	// custom adapter, used to intercept list item touch events.
    private class MyAdapter extends SimpleDragSortCursorAdapter {
        private Context mContext;

        public MyAdapter(Context ctxt, int rmid, Cursor c, String[] cols, int[] ids, int something) {
            super(ctxt, rmid, c, cols, ids, something);
            mContext = ctxt;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = super.getView(position, convertView, parent);
            View tv = v.findViewById(R.id.text);
            tv.setTag(position);
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                	int pos = (Integer) v.getTag();
                	Intent intent = new Intent(MainActivity.this, CustomizeWorkout.class);
    				Bundle extras = new Bundle();
    				extras.putInt("position", pos);
    				intent.putExtras(extras);
    				startActivityForResult(intent, WorkoutObjects.CUSTOMIZE_WORKOUT);
                }
            });
            return v;
        }
    }

}
