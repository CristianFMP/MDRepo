package md.smartitinerary.rest.model;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import org.postgis.LineString;

@XmlRootElement
public class Itinerary {
	private LineString poiLine;
	private List<Poi> pois; 
	private int popularity; 
	private double length;
	
	public Itinerary(LineString poiLine, List<Poi> pois, int popularity, double length) {
		this.poiLine = poiLine;
		this.pois = pois;
		this.popularity = popularity;
		this.length = length;
	}
	
	public Itinerary() {
		poiLine = null;
		pois = null;
		popularity = 0;
		length = 0;
	}

	public int getPopularity() {
		return popularity;
	}

	public double getLength() {
		return length;
	}

	public void setLenght(double length) {
		this.length = length;
	}

	public String getPoiLine() {
		return poiLine.toString();
	}

	public List<Poi> getPois() {
		return pois;
	}
	
	public String toString() {
		return poiLine + ", " + length + ", " + popularity;	
	}
}
