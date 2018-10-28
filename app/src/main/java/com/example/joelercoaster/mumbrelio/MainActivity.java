package com.example.joelercoaster.mumbrelio;

import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class MainActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button bForecast = findViewById(R.id.bForecast);
        final TextView tvTemp = findViewById(R.id.tvTemp);
        final TextView tvSummary = findViewById(R.id.tvSummary);
        final String pear = "Something has gone pear-shaped";
        final String noPermissions = "You have not allowed location permissions";

        final FusedLocationProviderClient client = LocationServices.getFusedLocationProviderClient(this);

        bForecast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(MainActivity.this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    tvTemp.setText(noPermissions);
                    return;
                }

                client.getLastLocation().addOnSuccessListener(MainActivity.this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            String lat = String.valueOf(location.getLatitude());
                            String lng = String.valueOf(location.getLongitude());
                            final String coords = lat + "," + lng;
                            new ForecastTask(tvTemp, tvSummary, coords).execute();
                        } else { tvTemp.setText(pear); }
                    }
                });
            }
        });
    }
}


