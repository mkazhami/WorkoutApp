package com.example.workout;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
/*
 * this will contain a list of all the exercises added by the user over time
 * to add to this list, press "+" on the screen and it will take you to customize exercise
 */
public class ExerciseListFrag extends DialogFragment{

	private ImageButton addExerciseButton;
	private static ExerciseListAdapter adapter;
	private ListView listView;
	
	private int position;
	
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		AlertDialog.Builder editExercise = new AlertDialog.Builder(getActivity());
    	final View view = getActivity().getLayoutInflater().inflate(R.layout.exercise_list, null);
    	editExercise.setView(view);
    	
    	listView = (ListView) view.findViewById(R.id.exerciseListView);
    	listView.setClickable(true);
    	
    	adapter = new ExerciseListAdapter(this, WorkoutObjects.exerciseNamesList);
    	listView.setAdapter(adapter);

		addExerciseButton = (ImageButton) view.findViewById(R.id.addExerciseButton);
		addExerciseButton.setOnClickListener(new OnClickListener(){
			public void onClick(View view){
				// start up the fragment that lets you create an exercise
				android.app.FragmentManager fm = ExerciseListFrag.this.getFragmentManager();
				CreateExercise f = new CreateExercise();
				f.show(fm, "dialog");
			}
		});
		
		return editExercise.create();
	}
	
	public static void updateList(String name) {
		//update the list with the given exercise
		int count = 0;
		//place the new exercise name in the proper (alphabetical) position
		for(String exerciseName : WorkoutObjects.exerciseNamesList) {
			if(name.compareTo(exerciseName) == 0) {
				return;
			}
			else if(name.compareTo(exerciseName) < 0) {
				WorkoutObjects.exerciseNamesList.add(count, name);
				break;
			}
			count++;
		}
		//if the string is greater than all the strings in the list, place it at the end
		if(count >= WorkoutObjects.exerciseNamesList.size()) {
			WorkoutObjects.exerciseNamesList.add(name);
		}
		adapter.notifyDataSetChanged();
	}
	
	public static void removeFromList(String name) {
		WorkoutObjects.exerciseNamesList.remove(name);
		adapter.notifyDataSetChanged();
	}
	
	public void dismissDialog() {
		getDialog().dismiss();
	}

}
