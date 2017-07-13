package com.example.xialong.funplacesforkids.util;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.content.ContextCompat;

import com.example.xialong.funplacesforkids.MainActivity;

import java.util.List;
import java.util.Locale;

/**
 * Created by administrator on 7/9/17.
 */

public class Util {

    public static String getCity(Context context, double lat, double lon){
        String city="";

        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        List<Address> addresses;
        try{
            addresses = geocoder.getFromLocation(lat, lon, 1);
            if(addresses.size()>0){
                city = addresses.get(0).getLocality();
            }else{
                city = "Unknown";
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return city;
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

    public static String formatTemp(String temp){
        int buffer = (int) Double.parseDouble(temp);
        return String.valueOf(buffer);
    }
}
