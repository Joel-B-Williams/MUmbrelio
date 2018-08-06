package com.example.joelercoaster.mumbrelio;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    TextView tvTemp;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvTemp = findViewById(R.id.tvTemp);

        ForecastTask forecastTask = new ForecastTask();
        forecastTask.execute();
    }

    class ForecastTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {

            //api.darksky.net/forecast/[key]/[latitude],[longitude]
            String response;
            try {
                URL url = new URL("https://api.darksky.net/forecast/cc49555736076755c5dee363e2b1fd21/41.8781,87.6298");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                try {
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                    response = readStream(in);
                    Log.d("LOOK AT ME", response);
                    return response;
                } catch(IOException e){

                } finally {
                    urlConnection.disconnect();
                }
            } catch (MalformedURLException e) {

            } catch (IOException e) {

            }
            return "Forecast Task Finished Without Response";
        }

        @Override
        protected void onPostExecute(String response) {
            tvTemp.setText(response);
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
}


