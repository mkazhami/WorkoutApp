package com.example.workout;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
/*
 * this will contain a list of all the exercises added by the user over time
 * to add to this list, press "+" on the screen and it will take you to customize exercise
 */
public class ExerciseListFrag extends DialogFragment{

	private ImageButton addExerciseButton;
	
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		//populate list
		AlertDialog.Builder editExercise = new AlertDialog.Builder(getActivity());
    	final View view = getActivity().getLayoutInflater().inflate(R.layout.exercise_list, null);
    	editExercise.setView(view);
		
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
	}

}
