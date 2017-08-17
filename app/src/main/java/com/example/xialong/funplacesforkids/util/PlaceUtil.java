package com.example.xialong.funplacesforkids.util;

import android.content.Context;
import android.util.Log;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.xialong.funplacesforkids.data.Place;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PlaceUtil {

    private static PlaceUtil mInstance;
    private static Context mContext;
    private RequestQueue mRequestQueue;
    private static final String KEY = "&key=AIzaSyAj4OYy0O9hkAPpgc7jzpc5LpwgpGGJkb8";
    private static final String TAG = PlaceUtil.class.getSimpleName();

    public interface PlaceCallback {
        void getResponse(String result);
    }

    private PlaceUtil(Context context) {
        mContext = context;
        mRequestQueue = getRequestQueue();
    }

    public static synchronized PlaceUtil getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new PlaceUtil(context);
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            Cache cache = new DiskBasedCache(mContext.getCacheDir(), 10 * 1024 * 1024);
            Network network = new BasicNetwork(new HurlStack());
            mRequestQueue = new RequestQueue(cache, network);
            mRequestQueue.start();
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

    public static void startVolleyRequest(final Context context, final PlaceCallback callback, String placeType) {
        String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=37.8,-122.4&radius=5000&types=" + placeType + KEY;
        Log.d("URL=", url);
        JsonObjectRequest jsObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject result) {
                callback.getResponse(result.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("api response=", "somethang wrong!");
            }
        });
        PlaceUtil.getInstance(context).addToRequestQueue(jsObjectRequest);
    }

    public static Place[] getPlaces(String result) throws JSONException {
        JSONObject object = new JSONObject(result);
        JSONArray jArray = object.getJSONArray("results");
        Place[] places = new Place[jArray.length()];
        Place place;
        String rating="";
        for (int i = 0; i < jArray.length(); i++) {
            JSONObject placeDetail = jArray.getJSONObject(i);
            String name = placeDetail.getString("name");
            String photoReference = placeDetail.getJSONArray("photos").getJSONObject(0).getString("photo_reference");
            String imageUrl = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=" + photoReference + KEY;
            try{
                rating = placeDetail.getDouble("rating") + "";
            }catch (Exception e){
                rating = "0";
            }

            String address = placeDetail.getString("vicinity");
            place = new Place(name, imageUrl, rating, address);
            places[i] = place;
        }
//        Log.d(TAG, places.length + "");
//        Log.d(TAG, places[0].getPlaceAddress() + " " + places[0].getPlaceName() + " " + places[0].getPlaceImageUrl() + " " + places[0].getPlaceRating());
        return places;
    }
}
