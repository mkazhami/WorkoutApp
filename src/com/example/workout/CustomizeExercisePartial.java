package com.example.workout;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class CustomizeExercisePartial extends DialogFragment{
	
	private Exercise exercise;
	
	private EditText editName;
	private EditText editReps;
	private EditText editSets;
	
	private String name;
	private String reps = "";
	private String sets = "";
	
	private int position;
	
	@Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
		Bundle bundle = getArguments();
		exercise = bundle.getParcelable(WorkoutObjects.EXER_KEY);
		position = bundle.getInt("position");
		
		AlertDialog.Builder editExercise = new AlertDialog.Builder(getActivity());
    	final View view = getActivity().getLayoutInflater().inflate(R.layout.customize_exercise_partial, null);
    	editExercise.setView(view);
    	
    	editName = (EditText) view.findViewById(R.id.PartialCustomizeNameField);
    	editReps = (EditText) view.findViewById(R.id.PartialCustomizeRepsField);
    	editSets = (EditText) view.findViewById(R.id.PartialCustomizeSetsField);
    	editName.setKeyListener(null);
    	
    	name = exercise.getName();
		reps = exercise.getReps();
		sets = exercise.getSets();
		editName.setText(name);
		editReps.setText(reps);
		editSets.setText(sets);
		
		editExercise.setTitle("Customize Exercise");
        editExercise.setPositiveButton("Done", new PositiveButtonClickListener());
        editExercise.setNegativeButton("Cancel", new NegativeButtonClickListener());
        
        setFieldListeners();
        
        return editExercise.create();
	}
	
	private boolean isValid(String num) {
		if(num == null) return false;
		int length = num.length();
		if(length == 0) return false;
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
        	//SHOULDNT EXIT DIALOG IF INVALID
        	if(!isValid(reps)) {
        		Toast toast = Toast.makeText(getActivity(), "Invalid reps!", Toast.LENGTH_SHORT);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
        		return;
        	}
        	if(!isValid(sets)) {
        		Toast toast = Toast.makeText(getActivity(), "Invalid sets!", Toast.LENGTH_SHORT);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
        		return;
        	}
        	if (exercise == null) exercise = new Exercise();
        	exercise.setName(name);
        	exercise.setReps(reps);
        	exercise.setSets(sets);
        	
        	EditedExercise returnActivity = (EditedExercise) getActivity();
        	returnActivity.onEdit(exercise, position);
        	//FileManagement.writeExerciseFile();
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
		TextWatcher repsListener = new TextWatcher(){
			@Override
			public void afterTextChanged(Editable arg0) {}
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,int arg3) {}
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,int arg3) {
				String temp = reps;
				reps = arg0.toString();
				if(!isValid(reps)) {
					Toast toast = Toast.makeText(getActivity(), "Invalid reps!", Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
					reps = temp;
	        	}
			}
		};
		
		TextWatcher setsListener = new TextWatcher(){
			@Override
			public void afterTextChanged(Editable arg0) {
				String temp = sets;
				sets = arg0.toString();
				if(!isValid(sets)) {
					Toast toast = Toast.makeText(getActivity(), "Invalid sets!", Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
					sets = temp;
	        	}
			}
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,int arg3) {}
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,int arg3) {
				String temp = sets;
				sets = arg0.toString();
				if(!isValid(sets)) {
					Toast toast = Toast.makeText(getActivity(), "Invalid sets!", Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
					sets = temp;
	        	}
			}
		};
		
		editReps.addTextChangedListener(repsListener);
		editSets.addTextChangedListener(setsListener);
	}
}
