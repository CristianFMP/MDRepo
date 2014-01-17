package md.smartitinerary.rest.model;

import javax.xml.bind.annotation.XmlRootElement;

import org.postgis.Point;
// Prova commit eclipse
@XmlRootElement
public class Poi {
	private Point poi;
	private String id;
	private String name;
	private double latitude;
	private double longitude;
	
	public Poi(Point poi, String id, String name) {
		this.poi = poi;
		this.id = id;
		this.name = name;
		this.latitude = poi.getX();
		this.longitude = poi.getY();
	}
	
	public Poi() {
		poi = null;
		id = "";
		name = "";
		latitude = 0.0;
		longitude = 0.0;
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
}
