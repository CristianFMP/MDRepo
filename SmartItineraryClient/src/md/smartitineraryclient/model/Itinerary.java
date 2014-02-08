package md.smartitineraryclient.model;

import java.text.DecimalFormat;
import java.util.List;


import android.os.Parcel;
import android.os.Parcelable;

public class Itinerary implements Parcelable {
	private List<Poi> pois; 
	private int popularity; 
	private double length;
	
	public Itinerary(List<Poi> pois, int popularity, double length) {
		this.pois = pois;
		this.popularity = popularity;
		this.length = length;
	}
	
	@SuppressWarnings("unchecked")
	private Itinerary(Parcel in) {
        pois = in.readArrayList(Poi.class.getClassLoader());
        popularity = in.readInt();
        length = in.readDouble();
    }
	
	public List<Poi> getPois() {
		return pois;
	}
	
	public int getPopularity() {
		return popularity;
	}

	public double getLength() {
		return length;
	}
	
	public double getLengthKm() {
		return Double.valueOf(new DecimalFormat("#,#").format(length/1000));
	}
	
	public int getLengthMeters() {
		return (int) length;
	}
	
	public String toString() {
		return pois + ", " + length + ", " + popularity;	
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeList(pois);
		out.writeInt(popularity);
		out.writeDouble(length);
	}
	
	public static final Parcelable.Creator<Itinerary> CREATOR = new Parcelable.Creator<Itinerary>() {
        public Itinerary createFromParcel(Parcel in) {
            return new Itinerary(in);
        }

        public Itinerary[] newArray(int size) {
            return new Itinerary[size];
        }
    };
}
