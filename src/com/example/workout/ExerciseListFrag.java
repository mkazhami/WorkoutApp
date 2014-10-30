package com.example.workout;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;
/*
 * this will contain a list of all the exercises added by the user over time
 * to add to this list, press "+" on the screen and it will take you to customize exercise
 */
public class ExerciseListFrag extends DialogFragment{

	private ImageButton addExerciseButton;
	private static ArrayAdapter<String> adapter;
	private ListView listView;
	
	private int position;
	
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		//Bundle bundle = getArguments();
		//position = bundle.getInt("position");

		AlertDialog.Builder editExercise = new AlertDialog.Builder(getActivity());
    	final View view = getActivity().getLayoutInflater().inflate(R.layout.exercise_list, null);
    	editExercise.setView(view);
    	
    	listView = (ListView) view.findViewById(R.id.exerciseListView);
    	listView.setClickable(true);
    	adapter = new ArrayAdapter<String>(getActivity(), R.layout.simplerow, WorkoutObjects.exerciseNamesList);
		//adapter.notifyDataSetChanged();   // may or may not need - got from StackOverflow
    	listView.setAdapter(adapter);
    	
		addExerciseButton = (ImageButton) view.findViewById(R.id.addExerciseButton);
		
		setUpButtonListeners();
		return editExercise.create();
	}
	
	private void setUpButtonListeners() {
    	//should take you to the customize exercise screen
    	addExerciseButton.setOnClickListener(new OnClickListener(){
			public void onClick(View view){
				// start up the fragment that lets you create an exercise
				android.app.FragmentManager fm = ExerciseListFrag.this.getFragmentManager();
				CustomizeExercise f = new CustomizeExercise();
				Bundle b = new Bundle();
				b.putInt("position", -1);
				f.setArguments(b);
				f.show(fm, "dialog");
			}
		});
    	
    	listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				String selectedExerciseName = (String) listView.getItemAtPosition(position);
				for(Exercise exercise : WorkoutObjects.exerciseList) {
					if(exercise.getName().equals(selectedExerciseName)) {
						//return exercise back to customize workout
						//Toast.makeText(getActivity(), "Exercise being added!" , Toast.LENGTH_LONG).show();
						EditedExercise returnActivity = (EditedExercise) getActivity();
						returnActivity.onEdit(exercise, -1);
						getDialog().dismiss();
					}
				}
			}
    		
		});
	}
	
	public static void updateList(Exercise exercise) {
		//update the list with the given exercise
		String newExerciseName = exercise.getName();
		int count = 0;
		//place the new exercise name in the proper (alphabetical) position
		for(String exerciseName : WorkoutObjects.exerciseNamesList) {
			if(newExerciseName.compareTo(exerciseName) < 0) {
				WorkoutObjects.exerciseNamesList.add(count, newExerciseName);
				break;
			}
			count++;
		}
		//if the string is greater than all the strings in the list, place it at the end
		if(count >= WorkoutObjects.exerciseNamesList.size()) {
			WorkoutObjects.exerciseNamesList.add(newExerciseName);
		}
		adapter.notifyDataSetChanged();
	}

}
