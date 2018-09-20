package com.example.joelercoaster.mumbrelio;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

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

class ForecastTask extends AsyncTask<String, Void, String> {

    private WeakReference<TextView> textView;

    public ForecastTask(TextView textView){
        this.textView = new WeakReference<>(textView);
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
            URL url = new URL("https://api.darksky.net/forecast/"+ BuildConfig.DarkSky + "/41.8781,-87.6298");
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
            String currentSummary = currentlyObject.getString("summary");
            String currentTemp = currentlyObject.getString("temperature");
            String display = currentSummary + " " + currentTemp;

            textView.get().setText(display);
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