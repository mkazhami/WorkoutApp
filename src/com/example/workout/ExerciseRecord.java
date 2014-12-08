package com.example.workout;

public class ExerciseRecord {

	private String name;
	private String[] sets;
	
	public ExerciseRecord(String name, int setsAmount) {
		this.name = name;
		this.sets = new String[setsAmount];
	}
	
	public void recordSet(int set, String amount) {
		this.sets[set] = amount;
	}
	
	public String getName() {
		return this.name;
	}
	
}
