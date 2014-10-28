package com.example.workout;

import android.os.Parcel;
import android.os.Parcelable;

public class Exercise implements Parcelable {
	private String exerciseName;
	private int numberOfReps;
	private int numberOfSets;
	
	public Exercise(){

	}
	
	public void setName(String name){
		this.exerciseName = name;
	}
	public void setReps(int num){
		this.numberOfReps = num;
	}
	public void setSets(int num){
		this.numberOfSets = num;
	}
	
	public String getName(){
		return this.exerciseName;
	}
	public int getSets(){
		return this.numberOfSets;
	}
	public int getReps(){
		return this.numberOfReps;
	}

	// Parcelling part
    public Exercise(Parcel in){
        Exercise ex = new Exercise();
        
        ex.setName(in.readString());
        ex.setReps(in.readInt());
        ex.setSets(in.readInt());
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Exercise createFromParcel(Parcel in) {
            return new Exercise(in); 
        }

		@Override
		public Object[] newArray(int arg0) {
			return null;
		}
    };

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parcel, int flags) {
		parcel.writeString(getName());
		parcel.writeInt(getReps());
		parcel.writeInt(getSets());	
	}
	
	
	
}
