package com.example.xialong.funplacesforkids.data;

import android.os.Parcel;
import android.os.Parcelable;

public class Place implements Parcelable {

    private String placeName;
    private String placeImageUrl;
    private String placeRating;
    private String placeAddress;

    public Place(String placeName, String placeImageUrl, String placeRating, String placeAddress){
        this.placeName = placeName;
        this.placeImageUrl = placeImageUrl;
        this.placeRating = placeRating;
        this.placeAddress = placeAddress;
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
        placeName = in.readString();
        placeImageUrl = in.readString();
        placeRating = in.readString();
        placeAddress = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

    }
}
