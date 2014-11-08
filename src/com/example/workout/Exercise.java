package com.example.workout;

import com.google.gson.Gson;

import android.os.Parcel;
import android.os.Parcelable;

public class Exercise implements Parcelable {
	private String exerciseName;
	private String numberOfReps;
	private String numberOfSets;
	
	public Exercise(){

	}
	
	public void setName(String name){
		this.exerciseName = name;
	}
	public void setReps(String num){
		this.numberOfReps = num;
	}
	public void setSets(String num){
		this.numberOfSets = num;
	}
	
	public String getName(){
		return this.exerciseName;
	}
	public String getSets(){
		return this.numberOfSets;
	}
	public String getReps(){
		return this.numberOfReps;
	}

	// Parcelling part
    public Exercise(Parcel in){
        Exercise ex = new Exercise();
        
        ex.setName(in.readString());
        ex.setReps(in.readString());
        ex.setSets(in.readString());
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
		parcel.writeString(getReps());
		parcel.writeString(getSets());	
	}
	
	public String convertToJson() {
		Gson gson = new Gson();
		return gson.toJson(this);		
	}
	
	
	
}
