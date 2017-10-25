package com.example.xialong.funplacesforkids.util;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class DetailUtil extends AsyncTask<String, Void, String> {

    private static final String KEY = "&key=AIzaSyAj4OYy0O9hkAPpgc7jzpc5LpwgpGGJkb8";
    private Context mContext;

    private DetailUtil(Context context){
        mContext = context;
    }

    @Override
    protected String doInBackground(String... args) {
        final String BASE_URL = "https://maps.googleapis.com/maps/api/place/details/json?";
        final String PLACE_ID_PARAM = "placeid";
        final String  KEY_PARAM = "key";

        HttpURLConnection connection;
        BufferedReader reader;
        String jsonStr = "";
        try {
            Uri uri = Uri.parse(BASE_URL).buildUpon().appendQueryParameter(PLACE_ID_PARAM, args[0])
                    .appendQueryParameter(KEY_PARAM, KEY).build();
            URL url = new URL(uri.toString());
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            InputStream inputStream = connection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null)
                return null;

            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                return null;
            }
            jsonStr = buffer.toString();
        }catch (IOException e){

        }
        return jsonStr;
    }

    @Override
    protected void onPostExecute(String result) {

    }
}
