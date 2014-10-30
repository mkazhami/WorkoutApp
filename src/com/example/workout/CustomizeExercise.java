package com.example.workout;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.google.gson.Gson;

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
	private int reps;
	private int sets;

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
    	
    	if (position >= 0) {
    		name = exercise.getName();
    		reps = exercise.getReps();
    		sets = exercise.getSets();
    		editName.setText(name);
    		editReps.setText(Integer.toString(reps));
    		editSets.setText(Integer.toString(sets));
    	}
    	else {
    		name = "";
    		reps = 0;
    		sets = 0;
    	}
    	
    	if(WorkoutObjects.exerciseNamesList.contains(name)) {
    		Toast.makeText(getActivity(), "Exercise already exists!" , Toast.LENGTH_SHORT).show();
    		editName.setText("");
    	}
    	
    	setFieldListeners();
    	
    	editExercise.setTitle("Customize Exercise");
        editExercise.setPositiveButton("Done", new PositiveButtonClickListener());
        editExercise.setNegativeButton("Cancel", new NegativeButtonClickListener());
         
        return editExercise.create();
    	
    }
	
    class PositiveButtonClickListener implements DialogInterface.OnClickListener
    {
        @Override
        public void onClick(DialogInterface dialog, int which)
        {
        	//EditedExercise returnActivity = (EditedExercise) getActivity();
        	if (exercise == null) exercise = new Exercise();
        	exercise.setName(name);
        	exercise.setReps(reps);
        	exercise.setSets(sets);
        	
        	File exerFile = null;
        	OutputStream out = null;
        	try {
        		exerFile = new File(WorkoutObjects.FOLDER_NAME + WorkoutObjects.EXERCISE_FILE_NAME);
				out = new FileOutputStream(exerFile);
				if(!exerFile.exists()) {
	        		exerFile.createNewFile();
	        	}
				out.write(exercise.convertToJson().getBytes());
				out.flush();
				out.close();
			} 
        	catch (FileNotFoundException e) { e.printStackTrace(); }
        	catch (IOException e) { e.printStackTrace(); }
        	
        	//returnActivity.onEdit(exercise, position);
        	WorkoutObjects.exerciseList.add(exercise); //also need to update files; json strings and shit
        	ExerciseListFrag.updateList(exercise);
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
				reps = Integer.parseInt(arg0.toString());
			}
		};
		
		TextWatcher setsListener = new TextWatcher(){
			@Override
			public void afterTextChanged(Editable arg0) {}
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,int arg3) {}
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,int arg3) {
				sets = Integer.parseInt(arg0.toString());
			}
		};
		
		editName.addTextChangedListener(nameListener);
		editReps.addTextChangedListener(repsListener);
		editSets.addTextChangedListener(setsListener);
		
	}
    	
}



