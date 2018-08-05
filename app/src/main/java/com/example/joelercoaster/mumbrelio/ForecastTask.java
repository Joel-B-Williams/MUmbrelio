package com.example.joelercoaster.mumbrelio;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ForecastTask extends AsyncTask<String, String, String> {

    @Override
    protected String doInBackground(String... urls) {

    //api.darksky.net/forecast/[key]/[latitude],[longitude]
        try {
        URL url = new URL("https://api.darksky.net/forecast/cc49555736076755c5dee363e2b1fd21/41.8781,87.6298");
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

        try {
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            String response = readStream(in);
            Log.d("LOOK AT ME", response);
        } catch(IOException e){

        } finally {
            urlConnection.disconnect();
        }
    } catch (MalformedURLException e) {

    } catch (IOException e) {

    }
    return "done";
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
            return "";
        }
    }
}
