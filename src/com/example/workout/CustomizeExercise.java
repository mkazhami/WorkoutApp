package com.example.workout;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class CustomizeExercise extends DialogFragment {

	private Exercise exercise;
	
	private EditText editName;
	private EditText editReps;
	private EditText editSets;
	
	private String name;
	private String reps;
	private String sets;

	int position;
	
	@Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
    	Bundle bundle = getArguments();

		exercise = bundle.getParcelable(WorkoutObjects.EXER_KEY);
		position = bundle.getInt("position");
    		
    	AlertDialog.Builder editExercise = new AlertDialog.Builder(getActivity());
    	final View view = getActivity().getLayoutInflater().inflate(R.layout.frag_customize_exercise, null);
    	editExercise.setView(view);
    	
    	editName = (EditText) view.findViewById(R.id.CustomizeNameField);
    	editReps = (EditText) view.findViewById(R.id.CustomizeRepsField);
    	editSets = (EditText) view.findViewById(R.id.CustomizeSetsField);
    	
		name = exercise.getName();
		reps = exercise.getReps();
		sets = exercise.getSets();
		editName.setText(name);
		editReps.setText(reps);
		editSets.setText(sets);
    	
    	setFieldListeners();
    	
    	editExercise.setTitle("Customize Exercise");
        editExercise.setPositiveButton("Done", new PositiveButtonClickListener());
        editExercise.setNegativeButton("Cancel", new NegativeButtonClickListener());
         
        return editExercise.create();
    }
	
	private boolean isValid(String num) {
		int length = num.length();
		for(int i = 0; i < length; i++) {
			char c = num.charAt(i);
			if(c > '9' || c < '0') {
				return false;
			}
		}
		return true;
	}
	
    class PositiveButtonClickListener implements DialogInterface.OnClickListener
    {
        @Override
        public void onClick(DialogInterface dialog, int which)
        {
        	if(WorkoutObjects.exerciseNamesList.contains(name) && position == -1) {
	    		Toast.makeText(getActivity(), "Exercise already exists!" , Toast.LENGTH_SHORT).show();
	    		editName.setText("");
	    		name = "";
	    		return;
	    	}
        	if(!isValid(reps)) {
        		Toast.makeText(getActivity(), "Invalid reps!", Toast.LENGTH_SHORT).show();
        		return;
        	}
        	if(!isValid(sets)) {
        		Toast.makeText(getActivity(), "Invalid sets!", Toast.LENGTH_SHORT).show();
        		return;
        	}
        	if (exercise == null) exercise = new Exercise();
        	exercise.setName(name);
        	exercise.setReps(reps);
        	exercise.setSets(sets);
        	
        	EditedExercise returnActivity = (EditedExercise) getActivity();
        	returnActivity.onEdit(exercise, position);
        	FileManagement.writeExerciseFile();
        	dialog.dismiss();
        }
    }
    
    class NegativeButtonClickListener implements DialogInterface.OnClickListener
    {
        @Override
        public void onClick(DialogInterface dialog, int which)
        {
            dialog.dismiss();
        }
        
    }
	
	private void setFieldListeners(){
		TextWatcher nameListener = new TextWatcher(){
			@Override
			public void afterTextChanged(Editable arg0) {}
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,int arg3) {}
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,int arg3) {
				name = arg0.toString();
			}
		};
		
		TextWatcher repsListener = new TextWatcher(){
			@Override
			public void afterTextChanged(Editable arg0) {}
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,int arg3) {}
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,int arg3) {
				reps = arg0.toString();
			}
		};
		
		TextWatcher setsListener = new TextWatcher(){
			@Override
			public void afterTextChanged(Editable arg0) {}
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,int arg3) {}
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,int arg3) {
				sets = arg0.toString();
			}
		};
		
		editName.addTextChangedListener(nameListener);
		editReps.addTextChangedListener(repsListener);
		editSets.addTextChangedListener(setsListener);
	}
    	
}



