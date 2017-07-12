package com.example.xialong.funplacesforkids.util;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOError;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeatherUtil extends AsyncTask<Double, Void, String> {

    private Context context;

    public interface Callback {
        void updateWeather(String temperature);
    }

    public WeatherUtil(Context context) {
        this.context = context;
    }

    protected String doInBackground(Double... params) {
        final String BASE_URL = "http://api.apixu.com/v1/current.json?key=cdcb793e00b64f96b92211923171207";
        final String API_QUERY_PARAM = "q";
        double lat = 37.68;
        double lon = -121.77;
        HttpURLConnection connection;
        BufferedReader reader;
        String jsonStr;
        try {
            Uri uri = Uri.parse(BASE_URL).buildUpon()
                    .appendQueryParameter(API_QUERY_PARAM, "" + lat + ", " + lon)
                    .build();

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

        } catch (IOException e) {
            e.getMessage();
            return null;
        }
        return jsonStr;
    }

    @Override
    protected void onPostExecute(String result) {
        String temp = "";
        try {
            JSONObject object = new JSONObject(result);
            JSONObject current = object.getJSONObject("current");
            JSONObject condition = current.getJSONObject("condition");
            temp = temp + current.getDouble("temp_c") + " "
                    + current.getDouble("temp_f")+" "
                    + condition.getString("icon");
        } catch (JSONException e) {
            e.getMessage();
        }
        ((Callback) context).updateWeather(temp);
    }
}
