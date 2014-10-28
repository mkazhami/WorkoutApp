package com.example.workout;

import java.util.ArrayList;

public class Workout {
	
	private String workoutName;	
	public ArrayList<Exercise> exercises;
	
	public Workout(){
		exercises = new ArrayList<Exercise>();
	}
	
	public void setName(String name){
		this.workoutName = name;
	}
	
	public void addExercise(Exercise exercise){
		this.exercises.add(exercise);
	}
	
	public Exercise getExercise(String name) {
		for (Exercise e : exercises) {
			if (e.getName().equals(name)) return e;
		}
		return null;
	}
	
	public String getName() {
		return workoutName;
	}
	
}
