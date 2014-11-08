package com.example.workout;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class FileManagement {

	public static void writeWorkoutFile() {
		try {
    		File wFile = new File(WorkoutObjects.FOLDER_NAME + WorkoutObjects.WORKOUT_FILE_NAME);
			OutputStream out = new FileOutputStream(wFile, false);
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(out));
			if(!wFile.exists()) {
        		wFile.createNewFile();
        	}
			int size = WorkoutObjects.workoutList.getSize();
			for(int i = 0; i < size; i++){ 
				Workout w = WorkoutObjects.workoutList.getWorkout(i);
				bw.write(w.convertToJson());
				bw.newLine();
			}
			bw.close();
		}
    	catch (FileNotFoundException e) { e.printStackTrace(); }
    	catch (IOException e) { e.printStackTrace(); }
	}
	
	public static void fillWorkoutList() {
		File wFile = new File(WorkoutObjects.FOLDER_NAME + WorkoutObjects.WORKOUT_FILE_NAME);
		if (!(wFile.exists())) {
			try { wFile.createNewFile(); } 
			catch (IOException e) { e.printStackTrace(); }
		}
		else {
			try {
				InputStream in = new FileInputStream(wFile);
				BufferedReader reader = new BufferedReader(new InputStreamReader(in));
				String line;
				while((line = reader.readLine()) != null) {
					Workout workout = Workout.convertFromJson(line);
					WorkoutObjects.workoutList.addWorkout(workout);
					WorkoutObjects.workoutNamesList.add(workout.getName());
				}
				reader.close();
				in.close();
			} 
			catch (FileNotFoundException e) {	e.printStackTrace(); } 
			catch (IOException e) {	e.printStackTrace(); }
		}
	}
	
	public static void writeExerciseFile() {
		try {
			File eFile = new File(WorkoutObjects.FOLDER_NAME + WorkoutObjects.EXERCISE_FILE_NAME);
			OutputStream out = new FileOutputStream(eFile, false);
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(out));
			if(!eFile.exists()) {
				eFile.createNewFile();
			}
			for(String name : WorkoutObjects.exerciseNamesList){ 
				bw.write(name);
				bw.newLine();
			}
			bw.close();
		}
		catch (FileNotFoundException e) { e.printStackTrace(); }
    	catch (IOException e) { e.printStackTrace(); }
	}

	public static void fillExerciseList() {
		File eFile = new File(WorkoutObjects.FOLDER_NAME + WorkoutObjects.EXERCISE_FILE_NAME);
		if (!(eFile.exists())) {
			try { eFile.createNewFile(); } 
			catch (IOException e) { e.printStackTrace(); }
		}
		else {
			try {
				InputStream in = new FileInputStream(eFile);
				BufferedReader reader = new BufferedReader(new InputStreamReader(in));
				String line;
				while((line = reader.readLine()) != null) {
					WorkoutObjects.exerciseNamesList.add(line);
				}
				reader.close();
				in.close();
			} 
			catch (FileNotFoundException e) {	e.printStackTrace(); } 
			catch (IOException e) {	e.printStackTrace(); }
		}
	}
}
