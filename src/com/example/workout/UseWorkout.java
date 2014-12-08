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
	
	ArrayList<ExerciseRecord> records;
	public int currentSet = 0;
	public int exerCount = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.use_workout);
		
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
		
		//fill the table with the exercises
		for(Exercise e: workout.getExercises()) {
			TableRow row = new TableRow(this);
			row.setBackgroundResource(R.drawable.cell_shape);
			row.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			TextView name = new TextView(this);
			name.setOnLongClickListener(new OnLongClickListener() {
				@Override
				public boolean onLongClick(View v) {
					//TODO: GATHER INFORMATION ABOUT EXERCISE
					//TODO: SHOW DIALOG WITH INFO ABOUT EXERCISE
					return false;
				}
			});
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
					int exerCode = (int) code/100;
					int copy = code;
					while(copy >= 100) {
						copy -= 100;
					}
					int set = copy;
					getRecord(exerCode).recordSet(set, s.toString());
				}
				@Override
				public void afterTextChanged(Editable s) {}
			});
		}
	}
	
	public ExerciseRecord getRecord(int code) {
		return records.get(code);
	}
}
