package md.smartitineraryclient.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Poi implements Parcelable {
	private String id;
	private String name;
	private String address;
	private int popularity;
	private double latitude;
	private double longitude;
	
	public Poi(String id, String name, String address, int popularity, double latitude, double longitude) {
		this.id = id;
		this.name = name;
		this.address = address;
		this.popularity = popularity;
		this.latitude = latitude;
		this.longitude = longitude;
	}
	
	public Poi(double longitude, double latitude) {
		this.longitude = longitude;
		this.latitude = latitude;
	}
	
	public Poi(Parcel in) {
		id = in.readString();
		name = in.readString();
		address = in.readString();
		popularity = in.readInt();
		latitude = in.readDouble();
		longitude = in.readDouble();
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
	
	public int getPopularity() {
		return popularity;
	}

	public double getLatitude() {
		return latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(id);
		dest.writeString(name);
		dest.writeString(address);
		dest.writeInt(popularity);
		dest.writeDouble(latitude);
		dest.writeDouble(longitude);
	}	
	
	public static final Parcelable.Creator<Poi> CREATOR = new Parcelable.Creator<Poi>() {
        public Poi createFromParcel(Parcel in) {
            return new Poi(in);
        }

        public Poi[] newArray(int size) {
            return new Poi[size];
        }
    };
}
