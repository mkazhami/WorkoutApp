package com.example.workout;

import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class UseWorkout extends Activity{

	private Workout workout;
	
	TableLayout workoutTable;
	
	ArrayList<ExerciseRecord> oldRecords;
	ArrayList<ExerciseRecord> records;
	public int currentSet = 0;
	public int exerCount = 0;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.use_workout);
		
		//keep old records in case they cancel the workout
		//FIX: will only merge list with global list if the 'record' button is pressed
		//TODO: delete this cloning process
		oldRecords = new ArrayList<ExerciseRecord>();
		for(ExerciseRecord er : WorkoutObjects.recordList) {
			oldRecords.add(er.clone());
		}
		
		Bundle bundle = this.getIntent().getExtras();
		workout = new Workout();
		workout.setName(bundle.getString(WorkoutObjects.WORKOUT_NAME_KEY));
		ArrayList<Exercise> list = bundle.getParcelableArrayList(WorkoutObjects.WORKOUT_EXERCISES_KEY);
		workout.setExercises(list);
		
		workoutTable = (TableLayout) findViewById(R.id.useWorkoutTable);
		workoutTable.removeAllViews();
		
		records = new ArrayList<ExerciseRecord>();
		for(Exercise e: workout.getExercises()) {
			ExerciseRecord record = new ExerciseRecord(e.getName(), Integer.parseInt(e.getSets()));
			records.add(record);
		}
		
		final ArrayList<Pair<EditText, Integer>> editTextList = new ArrayList<Pair<EditText, Integer>>();
		final ArrayList<Pair<TextView, Integer>> textViewList = new ArrayList<Pair<TextView, Integer>>();
		final ArrayList<String> textViewNameList = new ArrayList<String>();
		final ArrayList<String> textViewSetsList = new ArrayList<String>();
		final ArrayList<String> textViewRepsList = new ArrayList<String>();
		final ArrayList<String> textViewInfoList = new ArrayList<String>();
		
		//fill the table with the exercises
		for(Exercise e: workout.getExercises()) {
			TableRow row = new TableRow(this);
			row.setBackgroundResource(R.drawable.cell_shape);
			row.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			TextView name = new TextView(this);
			textViewList.add(new Pair<TextView, Integer>(name, exerCount));
			textViewNameList.add(e.getName());
			textViewSetsList.add(e.getSets());
			textViewRepsList.add(e.getReps());
			textViewInfoList.add(e.getInfo());

			name.setPadding(10, 40, 30, 40);
			name.setGravity(Gravity.CENTER_VERTICAL);
			name.setText(e.getName());
			row.addView(name);
			for(int i = 0; i < Integer.parseInt(e.getSets()); i++) {
				currentSet = i;
				
				TextView divider = new TextView(this);
				divider.setBackgroundColor(Color.BLACK);
				divider.setWidth(2);
				divider.setPadding(50,40,50,40);
				row.addView(divider);
				
				EditText set = new EditText(this);
				set.setTextSize(14f);
				set.setWidth(150);
				set.setGravity(Gravity.CENTER);
				editTextList.add(new Pair<EditText, Integer>(set, (Integer) (exerCount*100 + i)));
				row.addView(set);
				
				if(i == Integer.parseInt(e.getSets()) -1) {
					TextView divider2 = new TextView(this);
					divider2.setBackgroundColor(Color.BLACK);
					divider2.setWidth(2);
					divider2.setPadding(50,40,50,40);
					row.addView(divider2);
				}
			}
			HorizontalScrollView sv = new HorizontalScrollView(this);
			sv.addView(row);
			workoutTable.addView(sv, new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			exerCount++;
		}
		
		for(Pair<EditText, Integer> pair: editTextList) {
			final int code = pair.getR();
			pair.getL().addTextChangedListener(new TextWatcher(){
				@Override
				public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {
					//code/100 will be the exercise number
					int exerCode = (int) code/100;
					int set = code;
					//the remainder of code/100 will be the set number
					//set variable might not be needed - exercise records will be kept for an exercise 
					//    name for all workouts in one spot
					while(set >= 100) {
						set -= 100;
					}
					getRecord(exerCode).recordSet(s.toString());
				}
				@Override
				public void afterTextChanged(Editable s) {}
			});
		}
		
		for(Pair<TextView, Integer> tvPair : textViewList) {
			final int pos = tvPair.getR();
			TextView tv = tvPair.getL();
			tv.setOnLongClickListener(new OnLongClickListener() {
				@Override
				public boolean onLongClick(View v) {
					Bundle bundle = new Bundle();
					bundle.putString("name", textViewNameList.get(pos));
					bundle.putString("sets", textViewSetsList.get(pos));
					bundle.putString("reps", textViewRepsList.get(pos));
					bundle.putString("info", textViewInfoList.get(pos));
					
					android.app.FragmentManager fm = UseWorkout.this.getFragmentManager();
					ExerciseInfoFrag f = new ExerciseInfoFrag();
					f.setArguments(bundle);
					f.show(fm, "dialog");
					return false;
				}
			});
		}
	}
	
	public ExerciseRecord getRecord(int code) {
		return records.get(code);
	}
	
	
	
	//TODO: add cancel and record buttons
	//cancel - delete all set records for this workout
	//		just don't call mergeRecordList - changes won't be saved to global list
	//		should just be a back (or cancel to be more specific) button that does nothing but go to parent activity
	//record - add this workout's records to the history:
	//		call mergeRecordList and pass in the records list, then go back to parent activity (?)
	
}
