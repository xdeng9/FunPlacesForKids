package com.example.xialong.funplacesforkids.util;

import android.content.Context;
import android.os.AsyncTask;

public class DetailUtil extends AsyncTask<String, Void, String> {

    private static final String KEY = "&key=AIzaSyAj4OYy0O9hkAPpgc7jzpc5LpwgpGGJkb8";
    private Context mContext;

    private DetailUtil(Context context){
        mContext = context;
    }

    @Override
    protected String doInBackground(String... args) {
        return null;
    }

    @Override
    protected void onPostExecute(String result) {

    }
}
