package md.smartitinerary.rest.model;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import org.postgis.LineString;

@XmlRootElement
public class Itinerary {
	private LineString poiLine; // da sistemare
	private List<Poi> pois; 
	private int popularity; 
	private double length;
	
	public Itinerary(LineString poiLine, List<Poi> pois, int popularity, double length) {
		this.poiLine = poiLine;
		this.setPois(pois);
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

	public void setPopularity(int popularity) {
		this.popularity = popularity;
	}

	public double getLength() {
		return length;
	}

	public void setLenght(double length) {
		this.length = length;
	}

	public LineString getPoiLine() {
		return poiLine;
	}

	public void setPoiLine(LineString poiLine) {
		this.poiLine = poiLine;
	}

	public List<Poi> getPois() {
		return pois;
	}

	public void setPois(List<Poi> pois) {
		this.pois = pois;
	}
}
