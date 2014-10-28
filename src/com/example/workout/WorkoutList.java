package com.example.workout;

import java.util.ArrayList;


public class WorkoutList {

	private static final WorkoutList instance = new WorkoutList();
	
	private static ArrayList<Workout> workoutList;
	
	public static WorkoutList getInstance() { 
		return instance;
	}
	
	private WorkoutList() {
		workoutList = new ArrayList<Workout>();
	}
	
	public void addWorkout(Workout workout){
		workoutList.add(workout);
	}
	
	public void removeWorkout(String name){
		int size = workoutList.size();
		for(int i = 0; i < size; i++){
			if(workoutList.get(i).getName().equals(name)){
				workoutList.remove(i);
			}
		}
	}
	
	public Workout getWorkout(String name) {
		
		int size = workoutList.size();
		for(int i = 0; i < size; i++){
			if(workoutList.get(i).getName().equals(name)){
				return workoutList.get(i);
			}
		}
		return null;
	}
	
}
