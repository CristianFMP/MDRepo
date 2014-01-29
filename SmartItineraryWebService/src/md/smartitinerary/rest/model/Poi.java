package md.smartitinerary.rest.model;

import javax.xml.bind.annotation.XmlRootElement;

import org.postgis.Point;

@XmlRootElement
public class Poi {
	private Point poi;
	private String id;
	private String name;
	private int popularity;
	private double latitude;
	private double longitude;
	
	public Poi(Point poi, String id, String name, int popularity) {
		this.poi = poi;
		this.id = id;
		this.name = name;
		this.popularity = popularity;
		this.latitude = poi.getX();
		this.longitude = poi.getY();
	}
	
	public Poi() {
		poi = new Point();
		id = "";
		name = "";
		popularity = 0;
		latitude = poi.getX();
		longitude = poi.getY();
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public double getLatitude() {
		return latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public Point getPoi() {
		return poi;
	}

	public void setPoi(Point poi) {
		this.poi = poi;
		this.latitude = poi.getX();
		this.longitude = poi.getY();
	}

	public int getPopularity() {
		return popularity;
	}

	public void setPopularity(int popularity) {
		this.popularity = popularity;
	}	
}
