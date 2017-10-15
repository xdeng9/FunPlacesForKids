package com.example.xialong.funplacesforkids.util;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.content.ContextCompat;

import com.example.xialong.funplacesforkids.MainActivity;
import com.example.xialong.funplacesforkids.R;
import com.example.xialong.funplacesforkids.data.PlaceContract;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class Util {
    public static final double SF_LAT = 37.8;
    public static final double SF_LON = -122.4;
    private static double mLat = SF_LAT;
    private static double mLon = SF_LON;
    private static String mCity = "Unkown";

    private static final ArrayList<String> tabNames = new ArrayList<String>() {{
        add("Amusement Parks");
        add("Aquariums");
        add("Museums");
        add("Bowling Alley");
        add("Parks");
        add("Zoos");
    }};

    private static final ArrayList<String> placeTypes = new ArrayList<String>() {{
        add("amusement_park");
        add("aquarium");
        add("museum");
        add("bowling_alley");
        add("park");
        add("zoo");
    }};

    public static String getCity(Context context, double lat, double lon) {
        String city = "";

        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        List<Address> addresses;
        try {
            addresses = geocoder.getFromLocation(lat, lon, 1);
            if (addresses.size() > 0) {
                city = addresses.get(0).getLocality();
            } else {
                city = "Unknown";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return city;
    }

    public static boolean cityChanged(Context context, Location location, String city) {
        return !city.equals(getCity(context, location.getLatitude(), location.getLongitude()));
    }

    public static boolean emptyData(final Context context) {
        boolean isEmpty = true;

        Cursor cursor = context.getContentResolver().query(
                PlaceContract.PlaceEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        if (cursor != null && cursor.getCount() != 0) {
            isEmpty = false;
        }
        cursor.close();
        return isEmpty;
    }

    public static Location getLocation(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String bestProvider = locationManager.getBestProvider(criteria, true);
        int permissionCheck = ContextCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_COARSE_LOCATION);
        Location location = null;
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            location = locationManager.getLastKnownLocation(bestProvider);
        }
        return location;
    }

    public static String formatTemp(String temp) {
        int buffer = (int) Double.parseDouble(temp);
        return String.valueOf(buffer) + "\u00b0" + "F";
    }

    public static int getRandomBackdrop() {
        ArrayList<Integer> backdrops = new ArrayList<Integer>() {{
            add(R.drawable.amusement_center);
            add(R.drawable.amusement_park);
            add(R.drawable.animal);
            add(R.drawable.aquarium);
            add(R.drawable.back_drop);
            add(R.drawable.beach);
            add(R.drawable.lake);
            add(R.drawable.montain);
            add(R.drawable.museum);
            add(R.drawable.park);
            add(R.drawable.sea);
            add(R.drawable.snow);
        }};
        Random rand = new Random();
        return backdrops.get(rand.nextInt(backdrops.size()));
    }

    public static ArrayList<String> getTabNames() {
        return tabNames;
    }

    public static ArrayList<String> getPlaceTypes() {
        return placeTypes;
    }

    public static float parseRating(String rating) {
        float result = 0f;

        try {
            result = Float.parseFloat(rating);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static double getCurrentLat() {
        return mLat;
    }

    public static double getCurrentLon() {
        return mLon;
    }

    public static void setCurrentLocation(double lat, double lon) {
        mLat = lat;
        mLon = lon;
    }

    public static String getCurrentCity() {
        return mCity;
    }

    public static void setCurrentCity(String city) {
        mCity = city;
    }

    public static String getDistance(double lat, double lon) {
        double distance = calcDistance(getCurrentLat(), getCurrentLon(), lat, lon);

        return String.valueOf(Math.round(distance * 10.0) / 10.0);
    }

    private static double calcDistance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
//        if (unit == "k") {
//            dist = dist * 1.609344;
//        } else if (unit == "m") {
//            dist = dist * 0.8684;
//        }
        return (dist * 0.8684);
    }

    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private static double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }
}
