package com.example.workout;

import com.example.workout.CustomizeExercise.NegativeButtonClickListener;
import com.example.workout.CustomizeExercise.PositiveButtonClickListener;

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

public class CreateExercise extends DialogFragment{

	private EditText editName;
	private String name;
	
	@Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder createExercise = new AlertDialog.Builder(getActivity());
    	final View view = getActivity().getLayoutInflater().inflate(R.layout.create_exercise, null);
    	createExercise.setView(view);
    	
    	editName = (EditText) view.findViewById(R.id.CreateExerciseNameField);
    	editName.addTextChangedListener(new TextWatcher() {
    		public void afterTextChanged(Editable s) {
    			name = s.toString();
    		}
    		public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
    	});
    	
    	
    	createExercise.setTitle("Create Exercise");
    	createExercise.setPositiveButton("Done", new PositiveButtonClickListener());
        createExercise.setNegativeButton("Cancel", new NegativeButtonClickListener());
    	return createExercise.create();
	}
	
	class PositiveButtonClickListener implements DialogInterface.OnClickListener
    {
        @Override
        public void onClick(DialogInterface dialog, int which)
        {
        	if(name == null || name.equals("")) {
        		Toast.makeText(getActivity(), "Invalid name!", Toast.LENGTH_SHORT).show();
        	}
        	ExerciseListFrag.updateList(name);
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
}
