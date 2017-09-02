package com.example.xialong.funplacesforkids.data;

import android.os.Parcel;
import android.os.Parcelable;

public class Place implements Parcelable {

    private String placeName;
    private String placeImageUrl;
    private String placeRating;
    private String placeAddress;
    private double latitude;
    private double longitude;
    private String placeId;

    public Place(String placeName, String placeImageUrl, String placeRating, String placeAddress, double latitude, double longitude, String placeId){
        this.placeName = placeName;
        this.placeImageUrl = placeImageUrl;
        this.placeRating = placeRating;
        this.placeAddress = placeAddress;
        this.latitude = latitude;
        this.longitude = longitude;
        this.placeId = placeId;
    }

    public String getPlaceName(){
        return placeName;
    }

    public String getPlaceImageUrl(){
        return placeImageUrl;
    }

    public String getPlaceRating(){
        return placeRating;
    }

    public String getPlaceAddress(){
        return placeAddress;
    }

    public double getLatitude() { return latitude;}

    public double getLongitude() { return longitude;}

    public String getPlaceId() { return placeId; }

    public static final Parcelable.Creator<Place> CREATOR
            = new Parcelable.Creator<Place>() {
        public Place createFromParcel(Parcel in) {
            return new Place(in);
        }

        public Place[] newArray(int size) {
            return new Place[size];
        }
    };

    private Place(Parcel in) {
        String[] data = new String[7];
        in.readStringArray(data);
        placeName = data[0];
        placeImageUrl = data[1];
        placeRating = data[2];
        placeAddress = data[3];
        latitude = Double.parseDouble(data[4]);
        longitude = Double.parseDouble(data[5]);
        placeId = data[6];
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeStringArray(new String[]{placeName,
        placeImageUrl, placeRating, placeAddress, String.valueOf(latitude), String.valueOf(longitude), placeId});
    }
}
