package md.smartitineraryclient.model;

import java.text.DecimalFormat;
import java.util.List;


import android.os.Parcel;
import android.os.Parcelable;

public class Itinerary implements Parcelable {
	private List<Poi> pois; 
	private int popularity; 
	private double length;
	private String add_date;
	private String pos_user;
	
	public Itinerary(List<Poi> pois, int popularity, double length) {
		this.pois = pois;
		this.popularity = popularity;
		this.length = length;
	}
	
	public Itinerary(List<Poi> pois, int popularity, double length, String pos_user, String add_date) {
		this.pois = pois;
		this.popularity = popularity;
		this.length = length;
		this.add_date = add_date;
		this.pos_user = pos_user;
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
	
	public String getAdd_date() {
		return add_date;
	}

	public void setAdd_date(String add_date) {
		this.add_date = add_date;
	}

	public String getPos_user() {
		return pos_user;
	}

	public void setPos_user(String pos_user) {
		this.pos_user = pos_user;
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
