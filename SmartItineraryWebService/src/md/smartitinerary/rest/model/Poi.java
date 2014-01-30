package md.smartitinerary.rest.model;

import javax.xml.bind.annotation.XmlRootElement;

import org.postgis.Point;

@XmlRootElement
public class Poi {
	private Point poi;
	private String id;
	private String name;
	private String address;
	private int popularity;
	private double latitude;
	private double longitude;
	
	public Poi(Point poi, String id, String name, String address, int popularity) {
		this.poi = poi;
		this.id = id;
		this.name = name;
		this.address = address;
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
	
	public String getName() {
		return name;
	}
	
	public String getAddress() {
		return address;
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

	public int getPopularity() {
		return popularity;
	}
}
