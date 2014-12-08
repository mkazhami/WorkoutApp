package com.example.workout;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.View;

public class WorkoutInfoFrag extends DialogFragment {
	
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder exerciseInfo = new AlertDialog.Builder(getActivity());
    	final View view = getActivity().getLayoutInflater().inflate(R.layout.exercise_info_frag, null);
    	exerciseInfo.setView(view);
    	
    	
    	
    	
    	return exerciseInfo.create();
	}

}
