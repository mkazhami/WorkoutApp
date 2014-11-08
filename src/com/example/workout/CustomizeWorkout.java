package com.example.workout;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CustomizeWorkout extends Activity implements EditedExercise {
	
	private Workout workout;
	
	private DragSortListView dslv;
	
	private static MyAdapter adapter;
	private static MatrixCursor cursor;
	
	private EditText editNameField;
	private Button addExerciseButton;
	
	private int position;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_workout);
		
		Bundle bundle = this.getIntent().getExtras();
		position = bundle.getInt("position");
		
		editNameField = (EditText) findViewById(R.id.workoutNameField);
		addExerciseButton = (Button) findViewById(R.id.addExerciseButton);

		//String workoutName = myIntent.getStringExtra("workoutName");
		if (position >= 0) {
			//workout = WorkoutList.getInstance().getWorkout(workoutName);
			workout = WorkoutList.getInstance().getWorkout(position);
			//editNameField.setText(workoutName);
			editNameField.setText(workout.getName());
		}
		else {
			workout = new Workout();
		}
		
		dslv = (DragSortListView) findViewById(R.id.exerciseList);
        
        String[] cols = {"name"};
        int[] ids = {R.id.text};
        adapter = new MyAdapter(this, R.layout.list_item_click_remove, null, cols, ids, 0);
        dslv.setAdapter(adapter);
        
        if (workout != null) {
		    // populate the list of exercises
		    cursor = new MatrixCursor(new String[] {"_id", "name"});
		    int size = workout.exercises.size();
			for (int i = 0; i < size; i++) {
				cursor.newRow()
					  .add(i)
		              .add(workout.exercises.get(i).getName());
			}
		    adapter.changeCursor(cursor);
        }
        
        setUpButtonListeners();
        setUpTextWatcher();
        
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
                	android.app.FragmentManager fm = CustomizeWorkout.this.getFragmentManager();
                    CustomizeExercisePartial customizeExerciseDialog = new CustomizeExercisePartial();
                    
                    Bundle bundle = new Bundle();
                    bundle.putParcelable(WorkoutObjects.EXER_KEY, workout.exercises.get(pos));
                    bundle.putInt("position", pos);
                    
                    customizeExerciseDialog.setArguments(bundle);
                    customizeExerciseDialog.show(fm, "customize_exercise_partial");
                }
            });
            return v;
        }
    }
	
    private void setUpButtonListeners() {
    	
    	addExerciseButton.setOnClickListener(new OnClickListener(){
			public void onClick(View view){
				// start up the fragment that lets you choose an exercise
				android.app.FragmentManager fm = CustomizeWorkout.this.getFragmentManager();
				ExerciseListFrag f = new ExerciseListFrag();
				f.show(fm, "dialog");
			}
		});
    }
    
    private void setUpTextWatcher(){
    	TextWatcher nameListener = new TextWatcher(){
			@Override
			public void afterTextChanged(Editable arg0) {}
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,int arg3) {}
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,int arg3) {
				workout.setName(arg0.toString());
			}
		};
		editNameField.addTextChangedListener(nameListener);
    }
    
    
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_workout, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
	    switch (item.getItemId()) {
	    	case R.id.action_exit:
	    		//EXIT
	    		setResult(WorkoutObjects.BAD_RESULT);
	            finish();
	            return false; // ???????
	        case R.id.action_save:
	        	//SAVE
	        	checkRemoved();
	        	if(position == -1) MainActivity.updateList(workout);
	        	MainActivity.onEdit(workout, position);
	        	FileManagement.writeWorkoutFile();
	        	setResult(WorkoutObjects.OK_RESULT);
	        	finish();
	        	return false;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	private void checkRemoved() {
		ArrayList<Integer> positions = adapter.getCursorPositions();
		ArrayList<Exercise> newExercises = new ArrayList<Exercise>();
		if (cursor != null) {
			cursor.moveToFirst();
	        for (int i = 0; i < positions.size(); i++) {
	        	int cursorPosition = positions.get(i);
	        	cursor.moveToPosition(cursorPosition);
	        	String c = cursor.getString(1);
	        	newExercises.add(workout.getExercise(c));
	        }
	        workout.exercises = newExercises;
		}
	}

	@Override
	public void onEdit(Exercise exercise, int position) {
		if(position == -1) {
			for(Exercise e : workout.exercises) {
				String name = e.getName();
				if(name.equals(exercise.getName())) {
		    		Toast toast = Toast.makeText(this, "Exercise already exists!", Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
		    		return;
		    	}
			}
		}
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
	        			c = exercise.getName();
	        		}
	        	}
	        	newcursor.newRow()
	                    .add(listPosition)
	                    .add(c);
	        }
		}
        // add the new row
        if (position < 0 && exercise != null) {
        	newcursor.newRow().add(newcursor.getCount()).add(exercise.getName());
        }
        
        
        ArrayList<Exercise> newExercises = new ArrayList<Exercise>();
        newcursor.moveToFirst();
        for (int i = 0; i < newcursor.getCount(); i++) {
        	String ename = newcursor.getString(1);
        	Exercise e = workout.getExercise(ename);
        	if (e != null) newExercises.add(e);
        	else if (i == newcursor.getCount() - 1) newExercises.add(exercise);
        	if (!newcursor.moveToNext()) break;
        }
         
        adapter.changeCursor(newcursor); 
        cursor = newcursor;
		
        // modify workout to reflect the change
        workout.exercises = newExercises;
        
	}

}
