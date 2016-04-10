package com.example.h_sed.test2.mapping;

import com.google.gson.annotations.SerializedName;

/**
 * Created by hamid on 08/05/2015.
 */
public  class Navigation {

	@SerializedName("id")
	public int myId;
	@SerializedName("name")
	public String myName;
	@SerializedName("fromcentral")
	public FromCentral myFromCentral;
	@SerializedName("location")
	public Location myLocation;

	public int getMyId() {
		return myId;
	}

	public String getMyName() {
		return myName;
	}

	public FromCentral getMyFromCentral() {
		return myFromCentral;
	}

	public Location getMyLocation() {
		return myLocation;
	}

	public static class FromCentral {
		@SerializedName("car")
		private String myCar;
		@SerializedName("train")
		private String myTrain;

		public String getMyCar() {
			return myCar;
		}

		public String getMyTrain() {
			return myTrain;
		}

	}

	public static class Location {
		@SerializedName("latitude")
		private Double myLatitude;
		@SerializedName("longitude")
		private Double myLongitude;

		public Double getMyLatitude() {
			return myLatitude;
		}

		public Double getMyLongitude() {
			return myLongitude;
		}
	}
}
