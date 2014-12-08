package com.example.workout;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class ExerciseInfoFrag extends DialogFragment {
	
	private EditText name;
	private EditText sets;
	private EditText reps;
	private EditText info;
	
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder exerciseInfo = new AlertDialog.Builder(getActivity());
    	final View view = getActivity().getLayoutInflater().inflate(R.layout.exercise_info_frag, null);
    	exerciseInfo.setView(view);

    	//TODO: add youtube link to exercise video
    	
    	name = (EditText) view.findViewById(R.id.exerciseInfoNameField);
    	sets = (EditText) view.findViewById(R.id.exerciseInfoSetsField);
    	reps = (EditText) view.findViewById(R.id.exerciseInfoRepsField);
    	info = (EditText) view.findViewById(R.id.exerciseInfoInfoField);
    	
    	//set values of EditTexts
    	Bundle bundle = getArguments();
    	name.setText(bundle.getString("name"));
    	sets.setText(bundle.getString("sets"));
    	reps.setText(bundle.getString("reps"));
    	info.setText(bundle.getString("info"));
    	
    	//don't want EditTexts to be editable
    	name.setEnabled(false);
    	sets.setEnabled(false);
    	reps.setEnabled(false);
    	info.setEnabled(false);
    	
    	
    	return exerciseInfo.create();
	}

}
