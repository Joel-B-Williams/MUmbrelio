package com.example.joelercoaster.mumbrelio;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button bForecast = findViewById(R.id.bForecast);
        final TextView tvTemp = findViewById(R.id.tvTemp);

        bForecast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ForecastTask(tvTemp).execute();
            }
        });
    }
}


