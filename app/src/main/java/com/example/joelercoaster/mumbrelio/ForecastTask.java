package com.example.joelercoaster.mumbrelio;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

class ForecastTask extends AsyncTask<String, Void, String> {

    private final String coords;
    private WeakReference<TextView> tvTemp;
    private WeakReference<TextView> tvSummary;

    public ForecastTask(TextView textView, TextView textView1, String coords){
        this.tvTemp = new WeakReference<>(textView);
        this.tvSummary = new WeakReference<>(textView1);
        this.coords = coords; 
    }

//    // Leaky field - Task is linked to view, but what happens if view is destroyed while task is in motion??
//    private TextView sender;

//    public ForecastTask(TextView receiver){
//        this.sender = receiver;
//    }

    @Override
    protected String doInBackground(String... urls) {

        //api.darksky.net/forecast/[key]/[latitude],[longitude]
        try {
            URL url = new URL("https://api.darksky.net/forecast/"+ BuildConfig.DarkSky + "/" + coords + "?exclude=[daily,alerts,flags]" );
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            try {
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                return readStream(in);
            } catch(IOException e){
                return "486: Something has gone pear-shaped";
            } finally {
                urlConnection.disconnect();
            }
        } catch (MalformedURLException e) {
            return "486: Something has gone pear-shaped";
        } catch (IOException e) {
            return "486: Something has gone pear-shaped";
        }
    }

    @Override
    protected void onPostExecute(String response) {
        //Log.d("Response", response);
        try {
            JSONObject object = new JSONObject(response);
            JSONObject currentlyObject = object.getJSONObject("currently");
            JSONObject hourlyObject = object.getJSONObject("hourly");
            JSONArray hourlyData = hourlyObject.getJSONArray("data");
            ArrayList<Object> upcomingHours = new ArrayList<Object>();
            //TODO - make 4 a magic number
            if ( hourlyData != null && hourlyData.length() >= 4 ) {
                for (int i=1;i<4;i++) {
                    upcomingHours.add(hourlyData.get(i).toString());
                }
                //TODO - add graph data for these points
            }

            String currentSummary = currentlyObject.getString("summary");
            String apparentTemp = currentlyObject.getString("apparentTemperature");

            tvSummary.get().setText(currentSummary);
            tvTemp.get().setText(apparentTemp);
            //Log.d("Response", object.toString(4));
            //Log.d("Hourly", hourlyObject.toString(4));
            //Log.d("hourlyData", hourlyData.toString(4));
            Log.d("upcoming Hours", upcomingHours.toString());
        } catch (JSONException e) {
        }
    }

    private String readStream(InputStream is) {
        try {
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            int i = is.read();
            while (i != -1) {
                bo.write(i);
                i = is.read();
            }
            return bo.toString();
        } catch (IOException e) {
            return "486: Something has gone pear-shaped";
        }
    }
}