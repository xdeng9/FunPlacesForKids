package com.example.xialong.funplacesforkids.util;

import android.content.Context;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;

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
}
