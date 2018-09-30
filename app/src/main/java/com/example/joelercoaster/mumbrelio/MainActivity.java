package com.example.joelercoaster.mumbrelio;

import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

public class MainActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button bForecast = findViewById(R.id.bForecast);
        final TextView tvTemp = findViewById(R.id.tvTemp);
        final String pear = "Something has gone pear-shaped";
        final FusedLocationProviderClient client = LocationServices.getFusedLocationProviderClient(this);

        bForecast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                client.getLastLocation().addOnSuccessListener(MainActivity.this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            String lat = String.valueOf(location.getLatitude());
                            String lng = String.valueOf(location.getLongitude());
                            final String coords = lat + "," + lng;
                            new ForecastTask(tvTemp, coords).execute();
                        } else {
                            tvTemp.setText(pear);
                        }
                    }
                });
            }
        });
    }
}


