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

import org.json.JSONException;
import org.json.JSONObject;

public class PlaceUtil {

    private static PlaceUtil mInstance;
    private static Context mContext;
    private RequestQueue mRequestQueue;

    private PlaceUtil (Context context){
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

    public static void startVolleyRequest(Context context, String placeType){
        String url="https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=37.8,-122.4&radius=500&types="+placeType+
                "&key=AIzaSyAj4OYy0O9hkAPpgc7jzpc5LpwgpGGJkb8";
        JsonObjectRequest jsObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("api response=",response.toString());
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){
                Log.d("api response=","somethang wrong!");
            }
        });
        PlaceUtil.getInstance(context).addToRequestQueue(jsObjectRequest);
    }

    public static Place[] getPlaces(String result){
        Place[] places = null;
        try {
            JSONObject object = new JSONObject(result);

        } catch (JSONException e) {
            e.getMessage();
        }

        return places;
    }
}
