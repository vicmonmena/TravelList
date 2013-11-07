package es.vicmonmena.openuax.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Esta clase contiene la informacion de un viaje. Los datos que almacena son la ciudad, el pais,
 * el anyo del viaje y una nota opcional.
 *
 * @author vicmonmena
 */
public class TravelInfo implements Parcelable {

	public static final String EXTRA_CITY = "extra_city";
	public static final String EXTRA_COUNTRY = "extra_country";
	public static final String EXTRA_YEAR = "extra_year";
	public static final String EXTRA_NOTE = "extra_note";
	public static final String EXTRA_TRAVEL = "extra_travel";
	
	private String city;
	private String country;
	private int year;
	private String note;
	
	public TravelInfo() {
		// TODO Auto-generated constructor stub
	}
	
	public TravelInfo(String city, String country, int year, String note){
		this.city = city;
		this.country = country;
		this.year = year;
		this.note = note;
	}
	
	public TravelInfo(String city, String country, int year){
		this(city, country, year, null);
	}
	
	public TravelInfo(Parcel travel) {
		readFromParcel(travel);
	}
	
	public String getCity() {
		return city;
	}
	
	public void setCity(String city) {
		this.city = city;
	}
	
	public String getCountry() {
		return country;
	}
	
	public void setCountry(String country) {
		this.country = country;
	}
	
	public int getYear() {
		return year;
	}
	
	public void setYear(int year) {
		this.year = year;
	}
	
	public void setNote(String note) {
		this.note = note;
	}
	
	public String getNote() {
		return note;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(year);
	    dest.writeString(country);
	    dest.writeString(city);
	    dest.writeString(note);
	}
	
	private void readFromParcel(Parcel in) {
		year = in.readInt();
	    country = in.readString();
	    city = in.readString();
	    note = in.readString();
    }
	
	public static final Parcelable.Creator<TravelInfo> CREATOR = 
		new Parcelable.Creator<TravelInfo>() {
        public TravelInfo createFromParcel(Parcel in) {
            return new TravelInfo(in);
        }
 
        public TravelInfo[] newArray(int size) {
            return new TravelInfo[size];
        }
    };
}
