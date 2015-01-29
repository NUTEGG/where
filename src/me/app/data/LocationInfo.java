package me.app.data;

public class LocationInfo {

	private double lat;
	private double lng;
	private String time;

	public LocationInfo(double lat, double lng, String time) {
		this.lat = lat;
		this.lng = lng;
		this.time = time;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLng() {
		return lng;
	}

	public void setLng(double lng) {
		this.lng = lng;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

}
